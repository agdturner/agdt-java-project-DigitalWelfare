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

import java.util.HashSet;

/**
 *
 * @author geoagdt
 */
public class SHBE_DataRecord {

    /**
     * SRecords associated with a DRecord
     */
    protected HashSet<SHBE_DataRecord> SRecords;
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
     * 61 66 WeelklyClaimRelatedRent
     */
    private int WeelklyClaimRelatedRent;
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
     * 199 207 TransitionalProtectionFormNationalRolloutOfLHA
     */
    private int TransitionalProtectionFormNationalRolloutOfLHA;
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
    private int SourceOfMostRecentHBClaim;
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
     * @param RecordID 
     */
    public SHBE_DataRecord(
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
     * @param aSHBE_DataHandler
     * @throws java.lang.Exception
     */
    public SHBE_DataRecord(
            long RecordID,
            int type,
            String line,
            SHBE_DataRecord_Handler aSHBE_DataHandler) throws Exception {
        if (line.startsWith("S")) {
            //System.out.println("S record");
            this.RecordID = RecordID;
            String[] fields = line.split(",");
            if (fields.length != 275) {
                if (fields.length > 284) {
                    if (fields.length > 290) {
                        System.out.println("fields.length " + fields.length);
                        System.out.println("RecordID " + RecordID);
                        System.out.println(line);
                    }
                }
            }
            int n = 0;
            RecordType = fields[n];
            if (!aSHBE_DataHandler.getRecordTypes().contains(RecordType)) {
//            System.out.println("RecordType " + RecordType);
//            System.out.println("!aSHBE_DataHandler.getRecordTypes().contains(RecordType)");
                throw new Exception("!aSHBE_DataHandler.getRecordTypes().contains(RecordType)");
            }
            n++;
            HousingBenefitClaimReferenceNumber = fields[n];
            //HousingBenefitClaimReferenceNumber = Long.valueOf(fields[n]);
            n++;
            CouncilTaxBenefitClaimReferenceNumber = fields[n];
            //CouncilTaxBenefitClaimReferenceNumber = Long.valueOf(fields[n]);
            n++;
            ClaimantsNationalInsuranceNumber = fields[n];
            //ClaimantsTitle = fields[n];
            //ClaimantsSurname = fields[n];
            //ClaimantsFirstForename = fields[n;
            n++;
            ClaimantsDateOfBirth = fields[n];
            n++;
            if (fields[n].isEmpty()) {
                TenancyType = -999;
            } else {
                TenancyType = Integer.valueOf(fields[n]);
            }
//            if (TenancyType > 9 || TenancyType < 1) {
////            System.out.println("TenancyType " + TenancyType);
////            System.out.println("TenancyType > 9 || TenancyType < 1");
//                throw new Exception("TenancyType > 9 || TenancyType < 1");
//            }
            //ClaimantsAddressLine1 = fields[n];
            n++;
            ClaimantsPostcode = fields[n];
            n++; //7
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PassportedStandardIndicator = 0;
            } else {
                PassportedStandardIndicator = Integer.valueOf(fields[n]);
            }
//            if (PassportedStandardIndicator > 5 || PassportedStandardIndicator < 0) {
////            System.out.println("PassportedStandardIndicator " + PassportedStandardIndicator);
////            System.out.println("PassportedStandardIndicator > 5 || PassportedStandardIndicator < 0");
//                throw new Exception("PassportedStandardIndicator > 5 || PassportedStandardIndicator < 0");
//            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NumberOfChildDependents = 0;
            } else {
                NumberOfChildDependents = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NumberOfNonDependents = 0;
            } else {
                NumberOfNonDependents = Integer.valueOf(fields[n]);
            }
            n += 2; //10
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NonDependentStatus = 0;
            } else {
                NonDependentStatus = Integer.valueOf(fields[n]);
            }
//            if (NonDependentStatus > 8 || NonDependentStatus < 0) {
////            System.out.println("NonDependentStatus " + NonDependentStatus);
////            System.out.println("NonDependentStatus > 8 || NonDependentStatus < 0");
//                throw new Exception("NonDependentStatus > 8 || NonDependentStatus < 0");
//            }
            n++; //12
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NonDependentDeductionAmountApplied = 0;
            } else {
                NonDependentDeductionAmountApplied = Integer.valueOf(fields[n]);
            }
            n = 28;
            if (fields[n].isEmpty()) {
                StatusOfHBClaimAtExtractDate = -999;
            } else {
                StatusOfHBClaimAtExtractDate = Integer.valueOf(fields[n]);
            }
//            if (StatusOfHBClaimAtExtractDate > 2 || StatusOfHBClaimAtExtractDate < 0) {
////            System.out.println("StatusOfHBClaimAtExtractDate " + StatusOfHBClaimAtExtractDate);
////            System.out.println("StatusOfHBClaimAtExtractDate > 2 || StatusOfHBClaimAtExtractDate < 0");
//                throw new Exception("StatusOfHBClaimAtExtractDate > 2 || StatusOfHBClaimAtExtractDate < 0");
//            }
            n++;
            if (fields[n].isEmpty()) {
                StatusOfCTBClaimAtExtractDate = -999;
            } else {
                StatusOfCTBClaimAtExtractDate = Integer.valueOf(fields[n]);
            }
//            if (StatusOfCTBClaimAtExtractDate > 2 || StatusOfCTBClaimAtExtractDate < 0) {
////            System.out.println("StatusOfCTBClaimAtExtractDate " + StatusOfCTBClaimAtExtractDate);
////            System.out.println("StatusOfCTBClaimAtExtractDate > 2 || StatusOfCTBClaimAtExtractDate < 0");
//                throw new Exception("StatusOfCTBClaimAtExtractDate > 2 || StatusOfCTBClaimAtExtractDate < 0");
//            }
            n++; //30
            DateMostRecentHBClaimWasReceived = fields[n];
            n++;
            DateMostRecentCTBClaimWasReceived = fields[n];
            n++;
            DateOfFirstDecisionOnMostRecentHBClaim = fields[n];
            n++;
            DateOfFirstDecisionOnMostRecentCTBClaim = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                OutcomeOfFirstDecisionOnMostRecentHBClaim = 0;
            } else {
                OutcomeOfFirstDecisionOnMostRecentHBClaim = Integer.valueOf(fields[n]);
            }
//            if (OutcomeOfFirstDecisionOnMostRecentHBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentHBClaim < 0) {
////            System.out.println("OutcomeOfFirstDecisionOnMostRecentHBClaim " + OutcomeOfFirstDecisionOnMostRecentHBClaim);
////            System.out.println("OutcomeOfFirstDecisionOnMostRecentHBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentHBClaim < 0");
//                throw new Exception("OutcomeOfFirstDecisionOnMostRecentHBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentHBClaim < 0");
//            }
            n++; //35
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                OutcomeOfFirstDecisionOnMostRecentCTBClaim = 0;
            } else {
                OutcomeOfFirstDecisionOnMostRecentCTBClaim = Integer.valueOf(fields[n]);
            }
//            if (OutcomeOfFirstDecisionOnMostRecentCTBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentCTBClaim < 0) {
////            System.out.println("OutcomeOfFirstDecisionOnMostRecentCTBClaim " + OutcomeOfFirstDecisionOnMostRecentCTBClaim);
////            System.out.println("OutcomeOfFirstDecisionOnMostRecentCTBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentCTBClaim < 0");
//                throw new Exception("OutcomeOfFirstDecisionOnMostRecentCTBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentCTBClaim < 0");
//            }
            n++;
            HBClaimEntitlementStartDate = fields[n];
            n += 2;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyHousingBenefitEntitlement = 0;
            } else {
                WeeklyHousingBenefitEntitlement = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyCouncilTaxBenefitEntitlement = 0;
            } else {
                WeeklyCouncilTaxBenefitEntitlement = Integer.valueOf(fields[n]);
            }
            n++; //40
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                FrequencyOfPaymentOfHB = 0;
            } else {
                FrequencyOfPaymentOfHB = Integer.valueOf(fields[n]);
            }
            if (FrequencyOfPaymentOfHB > 99 || FrequencyOfPaymentOfHB < 0) {
//            System.out.println("FrequencyOfPaymentOfHB " + FrequencyOfPaymentOfHB);
//            System.out.println("FrequencyOfPaymentOfHB > 99 || FrequencyOfPaymentOfHB < 0");
                throw new Exception("FrequencyOfPaymentOfHB > 99 || FrequencyOfPaymentOfHB < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                FrequencyOfPaymentOfCTB = 0;
            } else {
                FrequencyOfPaymentOfCTB = Integer.valueOf(fields[n]);
            }
            if (FrequencyOfPaymentOfCTB != 3 && FrequencyOfPaymentOfCTB != 0) {
//            System.out.println("FrequencyOfPaymentOfCTB " + FrequencyOfPaymentOfCTB);
//            System.out.println("FrequencyOfPaymentOfCTB != 3 && FrequencyOfPaymentOfCTB != 0");
                throw new Exception("FrequencyOfPaymentOfCTB != 3 && FrequencyOfPaymentOfCTB != 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PreDeterminationAmountOfHB = 0;
            } else {
                PreDeterminationAmountOfHB = Integer.valueOf(fields[n]);
            }
            n++; //43
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PreDeterminationAmountOfCTB = 0;
            } else {
                PreDeterminationAmountOfCTB = Integer.valueOf(fields[n]);
            }
            n += 2; //45
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyHBEntitlementBeforeChange = 0;
            } else {
                WeeklyHBEntitlementBeforeChange = Integer.valueOf(fields[n]);
            }
            n += 2; //47
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyCTBEntitlementBeforeChange = 0;
            } else {
                WeeklyCTBEntitlementBeforeChange = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ReasonForDirectPayment = 0;
            } else {
                ReasonForDirectPayment = Integer.valueOf(fields[n]);
            }
//            if (ReasonForDirectPayment > 7 || ReasonForDirectPayment < 0) {
//                throw new Exception("ReasonForDirectPayment " + ReasonForDirectPayment + " > 7 || < 0");
//            }
            if (ReasonForDirectPayment > 8 || ReasonForDirectPayment < 0) {
                throw new Exception("ReasonForDirectPayment " + ReasonForDirectPayment + " > 8 || < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TimingOfPaymentOfRentAllowance = 0;
            } else {
                TimingOfPaymentOfRentAllowance = Integer.valueOf(fields[n]);
            }
            if (TimingOfPaymentOfRentAllowance > 4 || TimingOfPaymentOfRentAllowance < 0) {
//            System.out.println("TimingOfPaymentOfRentAllowance " + TimingOfPaymentOfRentAllowance);
//            System.out.println("TimingOfPaymentOfRentAllowance > 4 || TimingOfPaymentOfRentAllowance < 0");
                throw new Exception("TimingOfPaymentOfRentAllowance > 4 || TimingOfPaymentOfRentAllowance < 0");
            }
            n++; //50
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ExtendedPaymentCase = 0;
            } else {
                ExtendedPaymentCase = Integer.valueOf(fields[n]);
            }
            if (ExtendedPaymentCase > 4 || ExtendedPaymentCase < 0) {
//            System.out.println("ExtendedPaymentCase " + ExtendedPaymentCase);
//            System.out.println("ExtendedPaymentCase > 4 || ExtendedPaymentCase < 0");
                throw new Exception("ExtendedPaymentCase > 4 || ExtendedPaymentCase < 0");
            }
            n++;
            CouncilTaxBand = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyEligibleRentAmount = 0;
            } else {
                WeeklyEligibleRentAmount = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyEligibleCouncilTaxAmount = 0;
            } else {
                WeeklyEligibleCouncilTaxAmount = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("Y")) {
                ClaimantsStudentIndicator = "Y";
            } else {
                ClaimantsStudentIndicator = "N";
            }
            n++; //55
            if (fields[n].isEmpty()) {
                SecondAdultRebate = -999;
            } else {
                SecondAdultRebate = Integer.valueOf(fields[n]);
            }
//            if (SecondAdultRebate > 2 || SecondAdultRebate < 0) {
////            System.out.println("SecondAdultRebate " + SecondAdultRebate);
////            System.out.println("SecondAdultRebate > 2 || SecondAdultRebate < 1");
//                throw new Exception("SecondAdultRebate > 2 || SecondAdultRebate < 1");
//            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                RebatePercentageWhereASecondAdultRebateHasBeenAwarded = 0;
            } else {
                RebatePercentageWhereASecondAdultRebateHasBeenAwarded = Integer.valueOf(fields[n]);
            }
            if (RebatePercentageWhereASecondAdultRebateHasBeenAwarded > 4 || RebatePercentageWhereASecondAdultRebateHasBeenAwarded < 0) {
//            System.out.println("RebatePercentageWhereASecondAdultRebateHasBeenAwarded " + RebatePercentageWhereASecondAdultRebateHasBeenAwarded);
//            System.out.println("RebatePercentageWhereASecondAdultRebateHasBeenAwarded > 4 || RebatePercentageWhereASecondAdultRebateHasBeenAwarded < 0");
                throw new Exception("RebatePercentageWhereASecondAdultRebateHasBeenAwarded > 4 || RebatePercentageWhereASecondAdultRebateHasBeenAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                LHARegulationsApplied = "No";
            } else {
                LHARegulationsApplied = "Yes";
            }
            n++;
            if (fields[n].isEmpty()) {
                IsThisCaseSubjectToLRROrSRRSchemes = -999;
            } else {
                IsThisCaseSubjectToLRROrSRRSchemes = Integer.valueOf(fields[n]);
            }
//            if (IsThisCaseSubjectToLRROrSRRSchemes > 4 || IsThisCaseSubjectToLRROrSRRSchemes < 1) {
////            System.out.println("IsThisCaseSubjectToLRROrSRRSchemes " + IsThisCaseSubjectToLRROrSRRSchemes);
////            System.out.println("IsThisCaseSubjectToLRROrSRRSchemes > 4 || IsThisCaseSubjectToLRROrSRRSchemes < 1");
//                throw new Exception("IsThisCaseSubjectToLRROrSRRSchemes > 4 || IsThisCaseSubjectToLRROrSRRSchemes < 1");
//            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyLocalReferenceRent = 0;
            } else {
                WeeklyLocalReferenceRent = Integer.valueOf(fields[n]);
            }
            n++; //60
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklySingleRoomRent = 0;
            } else {
                WeeklySingleRoomRent = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeelklyClaimRelatedRent = 0;
            } else {
                WeelklyClaimRelatedRent = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                RentOfficerDeterminationOfIneligibleCharges = 0;
            } else {
                RentOfficerDeterminationOfIneligibleCharges = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyMaximumRent = 0;
            } else {
                WeeklyMaximumRent = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TotalDeductionForMeals = 0;
            } else {
                TotalDeductionForMeals = Integer.valueOf(fields[n]);
            }
            n++; //65
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyAdditionalDiscretionaryPayment = 0;
            } else {
                WeeklyAdditionalDiscretionaryPayment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ThirteenOrFiftyTwoWeekProtectionApplies = 0;
            } else {
                ThirteenOrFiftyTwoWeekProtectionApplies = Integer.valueOf(fields[n]);
            }
            if (ThirteenOrFiftyTwoWeekProtectionApplies > 1 || ThirteenOrFiftyTwoWeekProtectionApplies < 0) {
//            System.out.println("ThirteenOrFiftyTwoWeekProtectionApplies " + ThirteenOrFiftyTwoWeekProtectionApplies);
//            System.out.println("ThirteenOrFiftyTwoWeekProtectionApplies > 1 || ThirteenOrFiftyTwoWeekProtectionApplies < 0");
                throw new Exception("ThirteenOrFiftyTwoWeekProtectionApplies > 1 || ThirteenOrFiftyTwoWeekProtectionApplies < 0");
            }
            n++;
            DateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsAssessedIncomeFigure = 0;
            } else {
                ClaimantsAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsAdjustedAssessedIncomeFigure = 0;
            } else {
                ClaimantsAdjustedAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
            n++; //70
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsTotalCapital = 0;
            } else {
                ClaimantsTotalCapital = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsGrossWeeklyIncomeFromEmployment = 0;
            } else {
                ClaimantsGrossWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsNetWeeklyIncomeFromEmployment = 0;
            } else {
                ClaimantsNetWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsGrossWeeklyIncomeFromSelfEmployment = 0;
            } else {
                ClaimantsGrossWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsNetWeeklyIncomeFromSelfEmployment = 0;
            } else {
                ClaimantsNetWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
            n++; //75
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsTotalAmountOfEarningsDisregarded = 0;
            } else {
                ClaimantsTotalAmountOfEarningsDisregarded = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = 0;
            } else {
                ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromAttendanceAllowance = 0;
            } else {
                ClaimantsIncomeFromAttendanceAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromBusinessStartUpAllowance = 0;
            } else {
                ClaimantsIncomeFromBusinessStartUpAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromChildBenefit = 0;
            } else {
                ClaimantsIncomeFromChildBenefit = Integer.valueOf(fields[n]);
            }
            n++; //80
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent = 0;
            } else {
                ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromPersonalPension = 0;
            } else {
                ClaimantsIncomeFromPersonalPension = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromSevereDisabilityAllowance = 0;
            } else {
                ClaimantsIncomeFromSevereDisabilityAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromMaternityAllowance = 0;
            } else {
                ClaimantsIncomeFromMaternityAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromContributionBasedJobSeekersAllowance = 0;
            } else {
                ClaimantsIncomeFromContributionBasedJobSeekersAllowance = Integer.valueOf(fields[n]);
            }
            n++; //85
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromStudentGrantLoan = 0;
            } else {
                ClaimantsIncomeFromStudentGrantLoan = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromSubTenants = 0;
            } else {
                ClaimantsIncomeFromSubTenants = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromBoarders = 0;
            } else {
                ClaimantsIncomeFromBoarders = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromTrainingForWorkCommunityAction = 0;
            } else {
                ClaimantsIncomeFromTrainingForWorkCommunityAction = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromIncapacityBenefitShortTermLower = 0;
            } else {
                ClaimantsIncomeFromIncapacityBenefitShortTermLower = Integer.valueOf(fields[n]);
            }
            n++; //90
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromIncapacityBenefitShortTermHigher = 0;
            } else {
                ClaimantsIncomeFromIncapacityBenefitShortTermHigher = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromIncapacityBenefitLongTerm = 0;
            } else {
                ClaimantsIncomeFromIncapacityBenefitLongTerm = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromNewDeal50PlusEmploymentCredit = 0;
            } else {
                ClaimantsIncomeFromNewDeal50PlusEmploymentCredit = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromNewTaxCredits = 0;
            } else {
                ClaimantsIncomeFromNewTaxCredits = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent = 0;
            } else {
                ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent = Integer.valueOf(fields[n]);
            }
            n++; //95
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent = 0;
            } else {
                ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromGovernemntTraining = 0;
            } else {
                ClaimantsIncomeFromGovernemntTraining = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit = 0;
            } else {
                ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromCarersAllowance = 0;
            } else {
                ClaimantsIncomeFromCarersAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromStatutoryMaternityPaternityPay = 0;
            } else {
                ClaimantsIncomeFromStatutoryMaternityPaternityPay = Integer.valueOf(fields[n]);
            }
            n++; //100
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = 0;
            } else {
                ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP = 0;
            } else {
                ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromWarMobilitySupplement = 0;
            } else {
                ClaimantsIncomeFromWarMobilitySupplement = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromWidowsWidowersPension = 0;
            } else {
                ClaimantsIncomeFromWidowsWidowersPension = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromBereavementAllowance = 0;
            } else {
                ClaimantsIncomeFromBereavementAllowance = Integer.valueOf(fields[n]);
            }
            n++; //105
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromWidowedParentsAllowance = 0;
            } else {
                ClaimantsIncomeFromWidowedParentsAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromYouthTrainingScheme = 0;
            } else {
                ClaimantsIncomeFromYouthTrainingScheme = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromStatuatorySickPay = 0;
            } else {
                ClaimantsIncomeFromStatuatorySickPay = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsOtherIncome = 0;
            } else {
                ClaimantsOtherIncome = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsTotalAmountOfIncomeDisregarded = 0;
            } else {
                ClaimantsTotalAmountOfIncomeDisregarded = Integer.valueOf(fields[n]);
            }
            n++; //110
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                FamilyPremiumAwarded = 0;
            } else {
                FamilyPremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (FamilyPremiumAwarded > 1 || FamilyPremiumAwarded < 0) {
//            System.out.println("FamilyPremiumAwarded " + FamilyPremiumAwarded);
//            System.out.println("FamilyPremiumAwarded > 1 || FamilyPremiumAwarded < 0");
                throw new Exception("FamilyPremiumAwarded > 1 || FamilyPremiumAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                FamilyLoneParentPremiumAwarded = 0;
            } else {
                FamilyLoneParentPremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (FamilyLoneParentPremiumAwarded > 1 || FamilyLoneParentPremiumAwarded < 0) {
//            System.out.println("FamilyLoneParentPremiumAwarded " + FamilyLoneParentPremiumAwarded);
//            System.out.println("FamilyLoneParentPremiumAwarded > 1 || FamilyLoneParentPremiumAwarded < 0");
                throw new Exception("FamilyLoneParentPremiumAwarded > 1 || FamilyLoneParentPremiumAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                DisabilityPremiumAwarded = 0;
            } else {
                DisabilityPremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (DisabilityPremiumAwarded > 1 || DisabilityPremiumAwarded < 0) {
//            System.out.println("DisabilityPremiumAwarded " + DisabilityPremiumAwarded);
//            System.out.println("DisabilityPremiumAwarded > 1 || DisabilityPremiumAwarded < 0");
                throw new Exception("DisabilityPremiumAwarded > 1 || DisabilityPremiumAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                SevereDisabilityPremiumAwarded = 0;
            } else {
                SevereDisabilityPremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (SevereDisabilityPremiumAwarded > 1 || SevereDisabilityPremiumAwarded < 0) {
//            System.out.println("SevereDisabilityPremiumAwarded " + SevereDisabilityPremiumAwarded);
//            System.out.println("SevereDisabilityPremiumAwarded > 1 || SevereDisabilityPremiumAwarded < 0");
                throw new Exception("SevereDisabilityPremiumAwarded > 1 || SevereDisabilityPremiumAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                DisabledChildPremiumAwarded = 0;
            } else {
                DisabledChildPremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (DisabledChildPremiumAwarded > 1 || DisabledChildPremiumAwarded < 0) {
//            System.out.println("DisabledChildPremiumAwarded " + DisabledChildPremiumAwarded);
//            System.out.println("DisabledChildPremiumAwarded > 1 || DisabiledChildPremiumAwarded < 0");
                throw new Exception("DisabledChildPremiumAwarded > 1 || DisabiledChildPremiumAwarded < 0");
            }
            n++; //115
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                CarePremiumAwarded = 0;
            } else {
                CarePremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (CarePremiumAwarded > 1 || CarePremiumAwarded < 0) {
//            System.out.println("CarePremiumAwarded " + CarePremiumAwarded);
//            System.out.println("CarePremiumAwarded > 1 || CarePremiumAwarded < 0");
                throw new Exception("CarePremiumAwarded > 1 || CarePremiumAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                EnhancedDisabilityPremiumAwarded = 0;
            } else {
                EnhancedDisabilityPremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (EnhancedDisabilityPremiumAwarded > 1 || EnhancedDisabilityPremiumAwarded < 0) {
//            System.out.println("EnhancedDisabilityPremiumAwarded " + EnhancedDisabilityPremiumAwarded);
//            System.out.println("EnhancedDisabilityPremiumAwarded > 1 || EnhancedDisabilityPremiumAwarded < 0");
                throw new Exception("EnhancedDisabilityPremiumAwarded > 1 || EnhancedDisabilityPremiumAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                BereavementPremiumAwarded = 0;
            } else {
                BereavementPremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (BereavementPremiumAwarded > 1 || BereavementPremiumAwarded < 0) {
//            System.out.println("BereavementPremiumAwarded " + BereavementPremiumAwarded);
//            System.out.println("BereavementPremiumAwarded > 1 || BereavementPremiumAwarded < 0");
                throw new Exception("BereavementPremiumAwarded > 1 || BereavementPremiumAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnerFlag = 0;
            } else {
                PartnerFlag = Integer.valueOf(fields[n]);
            }
            if (PartnerFlag > 2 || PartnerFlag < 0) {
//            System.out.println("PartnerFlag " + PartnerFlag);
//            System.out.println("PartnerFlag > 2 || PartnerFlag < 0");
                throw new Exception("PartnerFlag > 2 || PartnerFlag < 0");
            }
            n++;
            PartnersStartDate = fields[n];
            n++; //120
            PartnersEndDate = fields[n];
            n++;
            PartnersNationalInsuranceNumber = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersStudentIndicator = "N";
            } else {
                PartnersStudentIndicator = "Y";
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersAssessedIncomeFigure = 0;
            } else {
                PartnersAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersAdjustedAssessedIncomeFigure = 0;
            } else {
                PartnersAdjustedAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
            n++; //125
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersGrossWeeklyIncomeFromEmployment = 0;
            } else {
                PartnersGrossWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersNetWeeklyIncomeFromEmployment = 0;
            } else {
                PartnersNetWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersGrossWeeklyIncomeFromSelfEmployment = 0;
            } else {
                PartnersGrossWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersNetWeeklyIncomeFromSelfEmployment = 0;
            } else {
                PartnersNetWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersTotalAmountOfEarningsDisregarded = 0;
            } else {
                PartnersTotalAmountOfEarningsDisregarded = Integer.valueOf(fields[n]);
            }
            n++; //130
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = 0;
            } else {
                PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromAttendanceAllowance = 0;
            } else {
                PartnersIncomeFromAttendanceAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromBusinessStartUpAllowance = 0;
            } else {
                PartnersIncomeFromBusinessStartUpAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromChildBenefit = 0;
            } else {
                PartnersIncomeFromChildBenefit = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromPersonalPension = 0;
            } else {
                PartnersIncomeFromPersonalPension = Integer.valueOf(fields[n]);
            }
            n++; //135
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromSevereDisabilityAllowance = 0;
            } else {
                PartnersIncomeFromSevereDisabilityAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromMaternityAllowance = 0;
            } else {
                PartnersIncomeFromMaternityAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromContributionBasedJobSeekersAllowance = 0;
            } else {
                PartnersIncomeFromContributionBasedJobSeekersAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromStudentGrantLoan = 0;
            } else {
                PartnersIncomeFromStudentGrantLoan = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromSubTenants = 0;
            } else {
                PartnersIncomeFromSubTenants = Integer.valueOf(fields[n]);
            }
            n++; //140
            if (fields[140].equalsIgnoreCase("")) {
                PartnersIncomeFromBoarders = 0;
            } else {
                PartnersIncomeFromBoarders = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromTrainingForWorkCommunityAction = 0;
            } else {
                PartnersIncomeFromTrainingForWorkCommunityAction = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromIncapacityBenefitShortTermLower = 0;
            } else {
                PartnersIncomeFromIncapacityBenefitShortTermLower = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromIncapacityBenefitShortTermHigher = 0;
            } else {
                PartnersIncomeFromIncapacityBenefitShortTermHigher = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromIncapacityBenefitLongTerm = 0;
            } else {
                PartnersIncomeFromIncapacityBenefitLongTerm = Integer.valueOf(fields[n]);
            }
            n++; //145
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromNewDeal50PlusEmploymentCredit = 0;
            } else {
                PartnersIncomeFromNewDeal50PlusEmploymentCredit = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromNewTaxCredits = 0;
            } else {
                PartnersIncomeFromNewTaxCredits = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromDisabilityLivingAllowanceCareComponent = 0;
            } else {
                PartnersIncomeFromDisabilityLivingAllowanceCareComponent = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent = 0;
            } else {
                PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromGovernemntTraining = 0;
            } else {
                PartnersIncomeFromGovernemntTraining = Integer.valueOf(fields[n]);
            }
            n++; //150
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromIndustrialInjuriesDisablementBenefit = 0;
            } else {
                PartnersIncomeFromIndustrialInjuriesDisablementBenefit = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromCarersAllowance = 0;
            } else {
                PartnersIncomeFromCarersAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromStatuatorySickPay = 0;
            } else {
                PartnersIncomeFromStatuatorySickPay = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromStatutoryMaternityPaternityPay = 0;
            } else {
                PartnersIncomeFromStatutoryMaternityPaternityPay = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = 0;
            } else {
                PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = Integer.valueOf(fields[n]);
            }
            n++; //155
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromWarDisablementPensionArmedForcesGIP = 0;
            } else {
                PartnersIncomeFromWarDisablementPensionArmedForcesGIP = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromWarMobilitySupplement = 0;
            } else {
                PartnersIncomeFromWarMobilitySupplement = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromWidowsWidowersPension = 0;
            } else {
                PartnersIncomeFromWidowsWidowersPension = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromBereavementAllowance = 0;
            } else {
                PartnersIncomeFromBereavementAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromWidowedParentsAllowance = 0;
            } else {
                PartnersIncomeFromWidowedParentsAllowance = Integer.valueOf(fields[n]);
            }
            n++; //160
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromYouthTrainingScheme = 0;
            } else {
                PartnersIncomeFromYouthTrainingScheme = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersOtherIncome = 0;
            } else {
                PartnersOtherIncome = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersTotalAmountOfIncomeDisregarded = 0;
            } else {
                PartnersTotalAmountOfIncomeDisregarded = Integer.valueOf(fields[n]);
            }
            n++;
            DateOverPaymentDetectionActivityInitiatedOnCase = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                MethodOfOverpayentDetectionActivity = 0;
            } else {
                MethodOfOverpayentDetectionActivity = Integer.valueOf(fields[n]);
            }
            if (MethodOfOverpayentDetectionActivity > 99 || MethodOfOverpayentDetectionActivity < 0) {
//            System.out.println("MethodOfOverpayentDetectionActivity " + MethodOfOverpayentDetectionActivity);
//            System.out.println("MethodOfOverpayentDetectionActivity > 99 || MethodOfOverpayentDetectionActivity < 0");
                throw new Exception("MethodOfOverpayentDetectionActivity > 99 || MethodOfOverpayentDetectionActivity < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ReasonForOverpaymentDetectionActivity = 0;
            } else {
                ReasonForOverpaymentDetectionActivity = Integer.valueOf(fields[n]);
            }
            if (ReasonForOverpaymentDetectionActivity > 99 || ReasonForOverpaymentDetectionActivity < 0) {
//            System.out.println("ReasonForOverpaymentDetectionActivity " + ReasonForOverpaymentDetectionActivity);
//            System.out.println("ReasonForOverpaymentDetectionActivity > 99 || ReasonForOverpaymentDetectionActivity < 0");
                throw new Exception("ReasonForOverpaymentDetectionActivity > 99 || ReasonForOverpaymentDetectionActivity < 0");
            }
            n++;
            DoesTheOverpaymentDetectionActivityConstituteAFullReview = fields[n];
            n++;
            DateOverPaymentDetectionActivityIsCompleted = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                OutcomeOfOverPaymentDetectionActivity = 0;
            } else {
                OutcomeOfOverPaymentDetectionActivity = Integer.valueOf(fields[n]);
            }
            if (OutcomeOfOverPaymentDetectionActivity > 99 || OutcomeOfOverPaymentDetectionActivity < 0) {
//            System.out.println("OutcomeOfOverPaymentDetectionActivity " + OutcomeOfOverPaymentDetectionActivity);
//            System.out.println("OutcomeOfOverPaymentDetectionActivity > 99 || OutcomeOfOverPaymentDetectionActivity < 0");
                throw new Exception("OutcomeOfOverPaymentDetectionActivity > 99 || OutcomeOfOverPaymentDetectionActivity < 0");
            }
            n++;
            ClaimantsGender = fields[n];
            n++; //170
            PartnersDateOfBirth = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                RentAllowanceMethodOfPayment = 0;
            } else {
                RentAllowanceMethodOfPayment = Integer.valueOf(fields[n]);
            }
            if (RentAllowanceMethodOfPayment > 99 || RentAllowanceMethodOfPayment < 0) {
//            System.out.println("RentAllowanceMethodOfPayment " + RentAllowanceMethodOfPayment);
//            System.out.println("RentAllowanceMethodOfPayment > 99 || RentAllowanceMethodOfPayment < 0");
                throw new Exception("RentAllowanceMethodOfPayment > 99 || RentAllowanceMethodOfPayment < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                RentAllowancePaymentDestination = 0;
            } else {
                RentAllowancePaymentDestination = Integer.valueOf(fields[n]);
            }
            if (RentAllowancePaymentDestination > 99 || RentAllowancePaymentDestination < 0) {
//            System.out.println("RentAllowancePaymentDestination " + RentAllowancePaymentDestination);
//            System.out.println("RentAllowancePaymentDestination > 99 || RentAllowancePaymentDestination < 0");
                throw new Exception("RentAllowancePaymentDestination > 99 || RentAllowancePaymentDestination < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ContractualRentAmount = 0;
            } else {
                ContractualRentAmount = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TimePeriodContractualRentFigureCovers = 0;
            } else {
                TimePeriodContractualRentFigureCovers = Integer.valueOf(fields[n]);
            }
            if (TimePeriodContractualRentFigureCovers > 99 || TimePeriodContractualRentFigureCovers < 0) {
//            System.out.println("TimePeriodContractualRentFigureCovers " + TimePeriodContractualRentFigureCovers);
//            System.out.println("TimePeriodContractualRentFigureCovers > 99 || TimePeriodContractualRentFigureCovers < 0");
                throw new Exception("TimePeriodContractualRentFigureCovers > 99 || TimePeriodContractualRentFigureCovers < 0");
            }
            n++; //175
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromPensionCreditSavingsCredit = 0;
            } else {
                ClaimantsIncomeFromPensionCreditSavingsCredit = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromPensionCreditSavingsCredit = 0;
            } else {
                PartnersIncomeFromPensionCreditSavingsCredit = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromMaintenancePayments = 0;
            } else {
                ClaimantsIncomeFromMaintenancePayments = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromMaintenancePayments = 0;
            } else {
                PartnersIncomeFromMaintenancePayments = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromOccupationalPension = 0;
            } else {
                ClaimantsIncomeFromOccupationalPension = Integer.valueOf(fields[n]);
            }
            n++; //180
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromOccupationalPension = 0;
            } else {
                PartnersIncomeFromOccupationalPension = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromWidowsBenefit = 0;
            } else {
                ClaimantsIncomeFromWidowsBenefit = Integer.valueOf(fields[n]);
            }
            n++; //182
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromWidowsBenefit = 0;
            } else {
                PartnersIncomeFromWidowsBenefit = Integer.valueOf(fields[n]);
            }
            n = 193; //193
            CTBClaimEntitlementStartDate = fields[n];
            n++;
            DateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective = fields[n];
            n++; //195
            DateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TotalNumberOfRooms = 0;
            } else {
                TotalNumberOfRooms = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NonSelfContainedAccomodation = 0;
            } else {
                NonSelfContainedAccomodation = Integer.valueOf(fields[n]);
            }
            if (NonSelfContainedAccomodation > 1 || NonSelfContainedAccomodation < 0) {
//            System.out.println("NonSelfContainedAccomodation " + NonSelfContainedAccomodation);
//            System.out.println("NonSelfContainedAccomodation > 1 || NonSelfContainedAccomodation < 0");
                throw new Exception("NonSelfContainedAccomodation > 1 || NonSelfContainedAccomodation < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TypeOfLHANumberOfRoomsEntitedTo = 0;
            } else {
                TypeOfLHANumberOfRoomsEntitedTo = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TransitionalProtectionFormNationalRolloutOfLHA = 0;
            } else {
                TransitionalProtectionFormNationalRolloutOfLHA = Integer.valueOf(fields[n]);
            }
            if (TransitionalProtectionFormNationalRolloutOfLHA > 1 || TransitionalProtectionFormNationalRolloutOfLHA < 0) {
//            System.out.println("TransitionalProtectionFormNationalRolloutOfLHA " + TransitionalProtectionFormNationalRolloutOfLHA);
//            System.out.println("TransitionalProtectionFormNationalRolloutOfLHA > 1 || TransitionalProtectionFormNationalRolloutOfLHA < 0");
                throw new Exception("TransitionalProtectionFormNationalRolloutOfLHA > 1 || TransitionalProtectionFormNationalRolloutOfLHA < 0");
            }
            n++; //200
            Locality = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ValueOfLHA = 0;
            } else {
                ValueOfLHA = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective = 0;
            } else {
                ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective = Integer.valueOf(fields[n]);
            }
            if (ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0) {
//            System.out.println("ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective " + ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective);
//            System.out.println("ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0");
                throw new Exception("ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective = 0;
            } else {
                ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective = Integer.valueOf(fields[n]);
            }
            if (ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0) {
//            System.out.println("ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective " + ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective);
//            System.out.println("ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0");
                throw new Exception("ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0");
            }
            n++;
            PartnersGender = fields[n];
            n++; //205
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NonDependantGrossWeeklyIncomeFromRemunerativeWork = 0;
            } else {
                NonDependantGrossWeeklyIncomeFromRemunerativeWork = Integer.valueOf(fields[n]);
            }
            n = 211;
            HBClaimTreatAsDateMade = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                SourceOfMostRecentHBClaim = 0;
            } else {
                SourceOfMostRecentHBClaim = Integer.valueOf(fields[n]);
            }
            if (SourceOfMostRecentHBClaim > 99 || SourceOfMostRecentHBClaim < 0) {
//            System.out.println("SourceOfMostRecentHBClaim " + SourceOfMostRecentHBClaim);
//            System.out.println("SourceOfMostRecentHBClaim > 99 || SourceOfMostRecentHBClaim < 0");
                throw new Exception("SourceOfMostRecentHBClaim > 99 || SourceOfMostRecentHBClaim < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim = 0;
            } else {
                DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim = Integer.valueOf(fields[n]);
            }
            if (DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim > 1 || DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim < 0) {
//            System.out.println("DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim " + DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim);
//            System.out.println("DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim > 1 || DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim < 0");
                throw new Exception("DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim > 1 || DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim < 0");
            }
            n++;
            DateOfFirstHBPaymentRentAllowanceOnly = fields[n];
            n++; //215
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly = 0;
            } else {
                WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly = Integer.valueOf(fields[n]);
            }
            if (WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly > 1 || WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly < 0) {
//            System.out.println("WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly " + WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly);
//            System.out.println("WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly > 1 || WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly < 0");
                throw new Exception("WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly > 1 || WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WasThereABackdatedAwardMadeOnTheHBClaim = 0;
            } else {
                WasThereABackdatedAwardMadeOnTheHBClaim = Integer.valueOf(fields[n]);
            }
            if (WasThereABackdatedAwardMadeOnTheHBClaim > 1 || WasThereABackdatedAwardMadeOnTheHBClaim < 0) {
//            System.out.println("WasThereABackdatedAwardMadeOnTheHBClaim " + WasThereABackdatedAwardMadeOnTheHBClaim);
//            System.out.println("WasThereABackdatedAwardMadeOnTheHBClaim > 1 || WasThereABackdatedAwardMadeOnTheHBClaim < 0");
                throw new Exception("WasThereABackdatedAwardMadeOnTheHBClaim > 1 || WasThereABackdatedAwardMadeOnTheHBClaim < 0");
            }
            n++;
            DateHBBackdatingFrom = fields[n];
            n++;
            DateHBBackdatingTo = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TotalAmountOfBackdatedHBAwarded = 0;
            } else {
                TotalAmountOfBackdatedHBAwarded = Integer.valueOf(fields[n]);
            }
            n++; //220
            CTBClaimTreatAsMadeDate = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                SourceOfTheMostRecentCTBClaim = 0;
            } else {
                SourceOfTheMostRecentCTBClaim = Integer.valueOf(fields[n]);
            }
            if (SourceOfTheMostRecentCTBClaim > 99 || SourceOfTheMostRecentCTBClaim < 0) {
//            System.out.println("SourceOfTheMostRecentCTBClaim " + SourceOfTheMostRecentCTBClaim);
//            System.out.println("SourceOfTheMostRecentCTBClaim > 99 || SourceOfTheMostRecentCTBClaim < 0");
                throw new Exception("SourceOfTheMostRecentCTBClaim > 99 || SourceOfTheMostRecentCTBClaim < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim = 0;
            } else {
                DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim = Integer.valueOf(fields[n]);
            }
            if (DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim > 1 || DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim < 0) {
//            System.out.println("DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim " + DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim);
//            System.out.println("DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim > 10 || DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim < 0");
                throw new Exception("DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim > 10 || DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WasThereABackdatedAwardMadeOnTheCTBClaim = 0;
            } else {
                WasThereABackdatedAwardMadeOnTheCTBClaim = Integer.valueOf(fields[n]);
            }
//        if (WasThereABackdatedAwardMadeOnTheCTBClaim > 1 || WasThereABackdatedAwardMadeOnTheCTBClaim < 0) {
////            System.out.println("WasThereABackdatedAwardMadeOnTheCTBClaim " + WasThereABackdatedAwardMadeOnTheCTBClaim);
////            System.out.println("WasThereABackdatedAwardMadeOnTheCTBClaim > 1 || WasThereABackdatedAwardMadeOnTheCTBClaim < 0");
//            throw new Exception("WasThereABackdatedAwardMadeOnTheCTBClaim > 1 || WasThereABackdatedAwardMadeOnTheCTBClaim < 0");
//        }
            n++;
            DateCTBBackdatingFrom = fields[n];
            n++; //225
            DateCTBBackdatingTo = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TotalAmountOfBackdatedCTBAwarded = 0;
            } else {
                TotalAmountOfBackdatedCTBAwarded = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly = 0;
            } else {
                InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly = Integer.valueOf(fields[n]);
            }
//        if (InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly > 1 || InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly < 0) {
////            System.out.println("InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly " + InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly);
////            System.out.println("InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly > 1 || InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly < 0");
//            throw new Exception("InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly > 1 || InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly < 0");
//        }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation = 0;
            } else {
                IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation = Integer.valueOf(fields[n]);
            }
//        if (IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation > 99 || IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation < 0) {
////            System.out.println("IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation " + IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation);
////            System.out.println("IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation > 99 || IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation < 0");
//            throw new Exception("IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation > 99 || IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation < 0");
//        }
            n++;
            DateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT = fields[n];
            n++; //230
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersTotalCapital = 0;
            } else {
                PartnersTotalCapital = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure = 0;
            } else {
                WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsTotalHoursOfRemunerativeWorkPerWeek = 0;
            } else {
                try {
                    ClaimantsTotalHoursOfRemunerativeWorkPerWeek = Double.valueOf(fields[n]);
                } catch (NumberFormatException e) {
                    e.printStackTrace(System.err);
                    throw e;
                }
            }
            n++;
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
            n = 236;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TotalHBPaymentsCreditsSinceLastExtract = 0;
            } else {
                TotalHBPaymentsCreditsSinceLastExtract = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TotalCTBPaymentsCreditsSinceLastExtract = 0;
            } else {
                TotalCTBPaymentsCreditsSinceLastExtract = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsEthnicGroup = 0;
            } else {
                ClaimantsEthnicGroup = Integer.valueOf(fields[n]);
            }
            if (ClaimantsEthnicGroup > 99 || ClaimantsEthnicGroup < 0) {
//            System.out.println("ClaimantsEthnicGroup " + ClaimantsEthnicGroup);
//            System.out.println("ClaimantsEthnicGroup > 99 || ClaimantsEthnicGroup < 0");
                throw new Exception("ClaimantsEthnicGroup > 99 || ClaimantsEthnicGroup < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NewWeeklyHBEntitlementAfterTheChange = 0;
            } else {
                NewWeeklyHBEntitlementAfterTheChange = Integer.valueOf(fields[n]);
            }
            n++; //240
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NewWeeklyCTBEntitlementAfterTheChange = 0;
            } else {
                NewWeeklyCTBEntitlementAfterTheChange = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TypeOfChange = 0;
            } else {
                TypeOfChange = Integer.valueOf(fields[n]);
            }
            if (TypeOfChange > 2 || TypeOfChange < 0) {
//            System.out.println("TypeOfChange " + TypeOfChange);
//            System.out.println("TypeOfChange > 2 || TypeOfChange < 0");
                throw new Exception("TypeOfChange > 2 || TypeOfChange < 0");
            }
            n++;
            DateLAFirstNotifiedOfChangeInClaimDetails = fields[n];
            n += 2;
            DateChangeOfDetailsAreEffectiveFrom = fields[n];
            n++; //245
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                IfNotAnnualUpratingHowWasTheChangeIdentified = 0;
            } else {
                IfNotAnnualUpratingHowWasTheChangeIdentified = Integer.valueOf(fields[n]);
            }
            if (IfNotAnnualUpratingHowWasTheChangeIdentified > 99 || IfNotAnnualUpratingHowWasTheChangeIdentified < 0) {
//            System.out.println("IfNotAnnualUpratingHowWasTheChangeIdentified " + IfNotAnnualUpratingHowWasTheChangeIdentified);
//            System.out.println("IfNotAnnualUpratingHowWasTheChangeIdentified > 99 || IfNotAnnualUpratingHowWasTheChangeIdentified < 0");
                throw new Exception("IfNotAnnualUpratingHowWasTheChangeIdentified > 99 || IfNotAnnualUpratingHowWasTheChangeIdentified < 0");
            }
            n++;
            DateSupercessionDecisionWasMadeOnTheHBClaim = fields[n];
            n++;
            DateSupercessionDecisionWasMadeOnTheCTBClaim = fields[n];
            n += 3; //250
            DateApplicationForRevisionReconsiderationReceived = fields[n];
            n++;
            DateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration = fields[n];
            n++;
            DateAppealApplicationWasLodged = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                OutcomeOfAppealApplication = 0;
            } else {
                OutcomeOfAppealApplication = Integer.valueOf(fields[n]);
            }
            if (OutcomeOfAppealApplication > 99 || OutcomeOfAppealApplication < 0) {
//            System.out.println("OutcomeOfAppealApplication " + OutcomeOfAppealApplication);
//            System.out.println("OutcomeOfAppealApplication > 99 || OutcomeOfAppealApplication < 0");
                throw new Exception("OutcomeOfAppealApplication > 99 || OutcomeOfAppealApplication < 0");
            }
            n++; //254
            DateOfOutcomeOfAppealApplication = fields[n];
            n += 6; //260
            DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim = fields[n];
            n++;
            DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim = fields[n];
            n++;
            DateCouncilTaxPayable = fields[n];
            n++;
            DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim = fields[n];
            n++;
            DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim = fields[n];
            n++; //265
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                SoftwareProvider = 0;
            } else {
                SoftwareProvider = Integer.valueOf(fields[n]);
            }
            if (SoftwareProvider > 3 || SoftwareProvider < 0) {
//            System.out.println("SoftwareProvider " + SoftwareProvider);
//            System.out.println("SoftwareProvider > 3 || SoftwareProvider < 0");
                throw new Exception("SoftwareProvider > 3 || SoftwareProvider < 0");
            }
            n++;
            StaffingFTE = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ContractingOutHandlingAndMaintenanceOfHBCTB = 0;
            } else {
                ContractingOutHandlingAndMaintenanceOfHBCTB = Integer.valueOf(fields[n]);
            }
            if (ContractingOutHandlingAndMaintenanceOfHBCTB > 9 || ContractingOutHandlingAndMaintenanceOfHBCTB < 0) {
//            System.out.println("ContractingOutHandlingAndMaintenanceOfHBCTB " + ContractingOutHandlingAndMaintenanceOfHBCTB);
//            System.out.println("ContractingOutHandlingAndMaintenanceOfHBCTB > 9 || ContractingOutHandlingAndMaintenanceOfHBCTB < 0");
                throw new Exception("ContractingOutHandlingAndMaintenanceOfHBCTB > 9 || ContractingOutHandlingAndMaintenanceOfHBCTB < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ContractingOutCounterFraudWorkRelatingToHBCTB = 0;
            } else {
                ContractingOutCounterFraudWorkRelatingToHBCTB = Integer.valueOf(fields[n]);
            }
            if (ContractingOutCounterFraudWorkRelatingToHBCTB > 9 || ContractingOutCounterFraudWorkRelatingToHBCTB < 0) {
//            System.out.println("ContractingOutCounterFraudWorkRelatingToHBCTB " + ContractingOutCounterFraudWorkRelatingToHBCTB);
//            System.out.println("ContractingOutCounterFraudWorkRelatingToHBCTB > 9 || ContractingOutCounterFraudWorkRelatingToHBCTB < 0");
                throw new Exception("ContractingOutCounterFraudWorkRelatingToHBCTB > 9 || ContractingOutCounterFraudWorkRelatingToHBCTB < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NumberOfBedroomsForLHARolloutCasesOnly = 0;
            } else {
                NumberOfBedroomsForLHARolloutCasesOnly = Integer.valueOf(fields[n]);
            }
            n++; //270
            PartnersDateOfDeath = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                JointTenancyFlag = 0;
            } else {
                JointTenancyFlag = Integer.valueOf(fields[n]);
            }
            if (JointTenancyFlag > 2 || JointTenancyFlag < 0) {
//            System.out.println("JointTenancyFlag " + JointTenancyFlag);
//            System.out.println("JointTenancyFlag > 2 || JointTenancyFlag < 0");
                throw new Exception("JointTenancyFlag > 2 || JointTenancyFlag < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                AppointeeFlag = 0;
            } else {
                AppointeeFlag = Integer.valueOf(fields[n]);
            }
            if (AppointeeFlag > 2 || AppointeeFlag < 0) {
//            System.out.println("AppointeeFlag " + AppointeeFlag);
//            System.out.println("AppointeeFlag > 2 || AppointeeFlag < 0");
                throw new Exception("AppointeeFlag > 2 || AppointeeFlag < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                RentFreeWeeksIndicator = 0;
            } else {
                RentFreeWeeksIndicator = Integer.valueOf(fields[n]);
            }
            n++; //274
            if (fields.length > n) {
                LastPaidToDate = fields[n];
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0;
                } else {
                    WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = Integer.valueOf(fields[n]);
                }
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    ClaimantsWeeklyIncomeFromESABasicElement = 0;
                } else {
                    ClaimantsWeeklyIncomeFromESABasicElement = Integer.valueOf(fields[n]);
                }
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    PartnersWeeklyIncomeFromESABasicElement = 0;
                } else {
                    PartnersWeeklyIncomeFromESABasicElement = Integer.valueOf(fields[n]);
                }
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    ClaimantsWeeklyIncomeFromESAWRAGElement = 0;
                } else {
                    ClaimantsWeeklyIncomeFromESAWRAGElement = Integer.valueOf(fields[n]);
                }
            }
            n++; //279
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    PartnersWeeklyIncomeFromESAWRAGElement = 0;
                } else {
                    PartnersWeeklyIncomeFromESAWRAGElement = Integer.valueOf(fields[n]);
                }
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    ClaimantsWeeklyIncomeFromESASCGElement = 0;
                } else {
                    ClaimantsWeeklyIncomeFromESASCGElement = Integer.valueOf(fields[n]);
                }
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    PartnersWeeklyIncomeFromESASCGElement = 0;
                } else {
                    PartnersWeeklyIncomeFromESASCGElement = Integer.valueOf(fields[n]);
                }
            }
            n++;
            if (fields.length > n) {
                WRAGPremiumFlag = fields[n];
            }
            n++; //283
            if (fields.length > n) {
                SCGPremiumFlag = fields[n];
            }
            if (type == 1) {
                n++;
                if (fields.length > n) {
                    LandlordPostcode = fields[n];
                }
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    SubRecordType = 0;
                } else {
                    try {
                        SubRecordType = Integer.valueOf(fields[n]);
                    } catch (NumberFormatException e) {
                        System.err.println("RecordID " + RecordID + " SubRecordType " + SubRecordType + " not converted to Integer");
                        e.printStackTrace(System.err);
                        throw e;
                    }
                }
                if (SubRecordType > 2 || SubRecordType < 0) {
//                System.out.println("SubRecordType " + SubRecordType);
//                System.out.println("SubRecordType > 2 || SubRecordType < 0");
                    throw new Exception("SubRecordType > 2 || SubRecordType < 0");
                }
            }
            if (type == 1) {
                n++;
                if (fields.length > n) {
                    SubRecordDateOfBirth = fields[n];
                }
            }
            n++;
            if (fields.length > n) {
                SubRecordChildReferenceNumberOrNINO = fields[n];
            }
            n++;
            if (fields.length > n) {
                SubRecordStartDate = fields[n];
            }
            n++;
            if (fields.length > n) {
                SubRecordEndDate = fields[n];
            }
            if (type == 1) {
                n++;
                if (fields.length > n) {
                    SubRecordDateOfBirth = fields[n];
                }
            }
            n++;
            if (fields.length > n) {
                IfThisActivityResolvesAnHBMSReferralProvideRMSNumber = fields[n];
            }
            n++;
            if (fields.length > n) {
                HBMSRuleScanID = fields[n];
            }
            n++;
            if (fields.length > n) {
                DateOfHBMSMatch = fields[n];
            }
            n++;
            if (fields.length > n) {
                IfResolutionOfHBMSReferralDoesNotResultInAFinancialAdjustmentPleaseIndicateTheReasonWhy = fields[n];
            }
            n++;
            if (fields.length > n) {
                UniqueTRecordIdentifier = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonCapital = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonClaimentPartnersEarnedIncome = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonNonDependentsEarnedIncome = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonPassportingStatus = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonIncomeFromDWPBenefits = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonTaxCredits = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonOtherIncome = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonLivingTogetherAsHusbandAndWife = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonNumberOfNonDependents = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonNumberOfDependents = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonNonResidence = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonEligibleRentCouncilTax = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonIneligible = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonIdentityDeath = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonOther = fields[n];
            }
            n++;
            if (fields.length > n) {
                BenefitThatThisPaymentErrorRelatesTo = fields[n];
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    TotalValueOfPaymentError = 0;
                } else {
                    try {
                        TotalValueOfPaymentError = Integer.valueOf(fields[n]);
                    } catch (NumberFormatException e) {
                        e.printStackTrace(System.err);
                        throw e;
                    }
                }
            }
            n++;
            if (fields.length > n) {
                WeeklyBenefitDiscrepancyAtStartOfPaymentError = fields[n];
            }
            n++;
            if (fields.length > n) {
                StartDateOfPaymentErrorPeriod = fields[n];
            }
            n++;
            if (fields.length > n) {
                EndDateOfPaymentErrorPeriod = fields[n];
            }
            n++;
            if (fields.length > n) {
                WhatWasTheCauseOfTheOverPayment = fields[n];
            }
        } else {
            //line.startsWith("D");
            //System.out.println("S record");
            this.RecordID = RecordID;
            String[] fields = line.split(",");
            if (fields.length != 275) {
                if (fields.length > 284) {
                    if (fields.length > 290) {
                        System.out.println("fields.length " + fields.length);
                        System.out.println("RecordID " + RecordID);
                        System.out.println(line);
                    }
                }
            }
            int n = 0;
            RecordType = fields[n];
            if (!aSHBE_DataHandler.getRecordTypes().contains(RecordType)) {
//            System.out.println("RecordType " + RecordType);
//            System.out.println("!aSHBE_DataHandler.getRecordTypes().contains(RecordType)");
                throw new Exception("!aSHBE_DataHandler.getRecordTypes().contains(RecordType)");
            }
            n++;
            HousingBenefitClaimReferenceNumber = fields[n];
            //HousingBenefitClaimReferenceNumber = Long.valueOf(fields[n]);
            n++;
            CouncilTaxBenefitClaimReferenceNumber = fields[n];
            //CouncilTaxBenefitClaimReferenceNumber = Long.valueOf(fields[n]);
            n++;
            ClaimantsNationalInsuranceNumber = fields[n];
            //ClaimantsTitle = fields[n];
            //ClaimantsSurname = fields[n];
            //ClaimantsFirstForename = fields[n;
            n++;
            ClaimantsDateOfBirth = fields[n];
            n++;
            if (fields[n].isEmpty()) {
                TenancyType = -999;
            } else {
                TenancyType = Integer.valueOf(fields[n]);
            }
            if (TenancyType > 9 || TenancyType < 1) {
//            System.out.println("TenancyType " + TenancyType);
//            System.out.println("TenancyType > 9 || TenancyType < 1");
                throw new Exception("TenancyType > 9 || TenancyType < 1");
            }
            //ClaimantsAddressLine1 = fields[n];
            n++;
            ClaimantsPostcode = fields[n];
            n++; //7
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PassportedStandardIndicator = 0;
            } else {
                PassportedStandardIndicator = Integer.valueOf(fields[n]);
            }
            if (PassportedStandardIndicator > 5 || PassportedStandardIndicator < 0) {
//            System.out.println("PassportedStandardIndicator " + PassportedStandardIndicator);
//            System.out.println("PassportedStandardIndicator > 5 || PassportedStandardIndicator < 0");
                throw new Exception("PassportedStandardIndicator > 5 || PassportedStandardIndicator < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NumberOfChildDependents = 0;
            } else {
                NumberOfChildDependents = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NumberOfNonDependents = 0;
            } else {
                NumberOfNonDependents = Integer.valueOf(fields[n]);
            }
            n += 2; //10
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NonDependentStatus = 0;
            } else {
                NonDependentStatus = Integer.valueOf(fields[n]);
            }
            if (NonDependentStatus > 8 || NonDependentStatus < 0) {
//            System.out.println("NonDependentStatus " + NonDependentStatus);
//            System.out.println("NonDependentStatus > 8 || NonDependentStatus < 0");
                throw new Exception("NonDependentStatus > 8 || NonDependentStatus < 0");
            }
            n++; //12
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NonDependentDeductionAmountApplied = 0;
            } else {
                NonDependentDeductionAmountApplied = Integer.valueOf(fields[n]);
            }
            n = 28;
            if (fields[n].isEmpty()) {
                StatusOfHBClaimAtExtractDate = -999;
            } else {
                StatusOfHBClaimAtExtractDate = Integer.valueOf(fields[n]);
            }
            if (StatusOfHBClaimAtExtractDate > 2 || StatusOfHBClaimAtExtractDate < 0) {
//            System.out.println("StatusOfHBClaimAtExtractDate " + StatusOfHBClaimAtExtractDate);
//            System.out.println("StatusOfHBClaimAtExtractDate > 2 || StatusOfHBClaimAtExtractDate < 0");
                throw new Exception("StatusOfHBClaimAtExtractDate > 2 || StatusOfHBClaimAtExtractDate < 0");
            }
            n++;
            if (fields[n].isEmpty()) {
                StatusOfCTBClaimAtExtractDate = -999;
            } else {
                StatusOfCTBClaimAtExtractDate = Integer.valueOf(fields[n]);
            }
            if (StatusOfCTBClaimAtExtractDate > 2 || StatusOfCTBClaimAtExtractDate < 0) {
//            System.out.println("StatusOfCTBClaimAtExtractDate " + StatusOfCTBClaimAtExtractDate);
//            System.out.println("StatusOfCTBClaimAtExtractDate > 2 || StatusOfCTBClaimAtExtractDate < 0");
                throw new Exception("StatusOfCTBClaimAtExtractDate > 2 || StatusOfCTBClaimAtExtractDate < 0");
            }
            n++; //30
            DateMostRecentHBClaimWasReceived = fields[n];
            n++;
            DateMostRecentCTBClaimWasReceived = fields[n];
            n++;
            DateOfFirstDecisionOnMostRecentHBClaim = fields[n];
            n++;
            DateOfFirstDecisionOnMostRecentCTBClaim = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                OutcomeOfFirstDecisionOnMostRecentHBClaim = 0;
            } else {
                OutcomeOfFirstDecisionOnMostRecentHBClaim = Integer.valueOf(fields[n]);
            }
            if (OutcomeOfFirstDecisionOnMostRecentHBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentHBClaim < 0) {
//            System.out.println("OutcomeOfFirstDecisionOnMostRecentHBClaim " + OutcomeOfFirstDecisionOnMostRecentHBClaim);
//            System.out.println("OutcomeOfFirstDecisionOnMostRecentHBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentHBClaim < 0");
                throw new Exception("OutcomeOfFirstDecisionOnMostRecentHBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentHBClaim < 0");
            }
            n++; //35
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                OutcomeOfFirstDecisionOnMostRecentCTBClaim = 0;
            } else {
                OutcomeOfFirstDecisionOnMostRecentCTBClaim = Integer.valueOf(fields[n]);
            }
            if (OutcomeOfFirstDecisionOnMostRecentCTBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentCTBClaim < 0) {
//            System.out.println("OutcomeOfFirstDecisionOnMostRecentCTBClaim " + OutcomeOfFirstDecisionOnMostRecentCTBClaim);
//            System.out.println("OutcomeOfFirstDecisionOnMostRecentCTBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentCTBClaim < 0");
                throw new Exception("OutcomeOfFirstDecisionOnMostRecentCTBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentCTBClaim < 0");
            }
            n++;
            HBClaimEntitlementStartDate = fields[n];
            n += 2;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyHousingBenefitEntitlement = 0;
            } else {
                WeeklyHousingBenefitEntitlement = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyCouncilTaxBenefitEntitlement = 0;
            } else {
                WeeklyCouncilTaxBenefitEntitlement = Integer.valueOf(fields[n]);
            }
            n++; //40
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                FrequencyOfPaymentOfHB = 0;
            } else {
                FrequencyOfPaymentOfHB = Integer.valueOf(fields[n]);
            }
            if (FrequencyOfPaymentOfHB > 99 || FrequencyOfPaymentOfHB < 0) {
//            System.out.println("FrequencyOfPaymentOfHB " + FrequencyOfPaymentOfHB);
//            System.out.println("FrequencyOfPaymentOfHB > 99 || FrequencyOfPaymentOfHB < 0");
                throw new Exception("FrequencyOfPaymentOfHB > 99 || FrequencyOfPaymentOfHB < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                FrequencyOfPaymentOfCTB = 0;
            } else {
                FrequencyOfPaymentOfCTB = Integer.valueOf(fields[n]);
            }
            if (FrequencyOfPaymentOfCTB != 3 && FrequencyOfPaymentOfCTB != 0) {
//            System.out.println("FrequencyOfPaymentOfCTB " + FrequencyOfPaymentOfCTB);
//            System.out.println("FrequencyOfPaymentOfCTB != 3 && FrequencyOfPaymentOfCTB != 0");
                throw new Exception("FrequencyOfPaymentOfCTB != 3 && FrequencyOfPaymentOfCTB != 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PreDeterminationAmountOfHB = 0;
            } else {
                PreDeterminationAmountOfHB = Integer.valueOf(fields[n]);
            }
            n++; //43
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PreDeterminationAmountOfCTB = 0;
            } else {
                PreDeterminationAmountOfCTB = Integer.valueOf(fields[n]);
            }
            n += 2; //45
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyHBEntitlementBeforeChange = 0;
            } else {
                WeeklyHBEntitlementBeforeChange = Integer.valueOf(fields[n]);
            }
            n += 2; //47
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyCTBEntitlementBeforeChange = 0;
            } else {
                WeeklyCTBEntitlementBeforeChange = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ReasonForDirectPayment = 0;
            } else {
                ReasonForDirectPayment = Integer.valueOf(fields[n]);
            }
//            if (ReasonForDirectPayment > 7 || ReasonForDirectPayment < 0) {
//                throw new Exception("ReasonForDirectPayment " + ReasonForDirectPayment + " > 7 || < 0");
//            }
            if (ReasonForDirectPayment > 8 || ReasonForDirectPayment < 0) {
                throw new Exception("ReasonForDirectPayment " + ReasonForDirectPayment + " > 8 || < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TimingOfPaymentOfRentAllowance = 0;
            } else {
                TimingOfPaymentOfRentAllowance = Integer.valueOf(fields[n]);
            }
            if (TimingOfPaymentOfRentAllowance > 4 || TimingOfPaymentOfRentAllowance < 0) {
//            System.out.println("TimingOfPaymentOfRentAllowance " + TimingOfPaymentOfRentAllowance);
//            System.out.println("TimingOfPaymentOfRentAllowance > 4 || TimingOfPaymentOfRentAllowance < 0");
                throw new Exception("TimingOfPaymentOfRentAllowance > 4 || TimingOfPaymentOfRentAllowance < 0");
            }
            n++; //50
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ExtendedPaymentCase = 0;
            } else {
                ExtendedPaymentCase = Integer.valueOf(fields[n]);
            }
            if (ExtendedPaymentCase > 4 || ExtendedPaymentCase < 0) {
//            System.out.println("ExtendedPaymentCase " + ExtendedPaymentCase);
//            System.out.println("ExtendedPaymentCase > 4 || ExtendedPaymentCase < 0");
                throw new Exception("ExtendedPaymentCase > 4 || ExtendedPaymentCase < 0");
            }
            n++;
            CouncilTaxBand = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyEligibleRentAmount = 0;
            } else {
                WeeklyEligibleRentAmount = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyEligibleCouncilTaxAmount = 0;
            } else {
                WeeklyEligibleCouncilTaxAmount = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("Y")) {
                ClaimantsStudentIndicator = "Y";
            } else {
                ClaimantsStudentIndicator = "N";
            }
            n++; //55
            if (fields[n].isEmpty()) {
                SecondAdultRebate = -999;
            } else {
                SecondAdultRebate = Integer.valueOf(fields[n]);
            }
            if (SecondAdultRebate > 2 || SecondAdultRebate < 0) {
//            System.out.println("SecondAdultRebate " + SecondAdultRebate);
//            System.out.println("SecondAdultRebate > 2 || SecondAdultRebate < 1");
                throw new Exception("SecondAdultRebate > 2 || SecondAdultRebate < 1");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                RebatePercentageWhereASecondAdultRebateHasBeenAwarded = 0;
            } else {
                RebatePercentageWhereASecondAdultRebateHasBeenAwarded = Integer.valueOf(fields[n]);
            }
            if (RebatePercentageWhereASecondAdultRebateHasBeenAwarded > 4 || RebatePercentageWhereASecondAdultRebateHasBeenAwarded < 0) {
//            System.out.println("RebatePercentageWhereASecondAdultRebateHasBeenAwarded " + RebatePercentageWhereASecondAdultRebateHasBeenAwarded);
//            System.out.println("RebatePercentageWhereASecondAdultRebateHasBeenAwarded > 4 || RebatePercentageWhereASecondAdultRebateHasBeenAwarded < 0");
                throw new Exception("RebatePercentageWhereASecondAdultRebateHasBeenAwarded > 4 || RebatePercentageWhereASecondAdultRebateHasBeenAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                LHARegulationsApplied = "No";
            } else {
                LHARegulationsApplied = "Yes";
            }
            n++;
            if (fields[n].isEmpty()) {
                IsThisCaseSubjectToLRROrSRRSchemes = -999;
            } else {
                IsThisCaseSubjectToLRROrSRRSchemes = Integer.valueOf(fields[n]);
            }
            if (IsThisCaseSubjectToLRROrSRRSchemes > 4 || IsThisCaseSubjectToLRROrSRRSchemes < 1) {
//            System.out.println("IsThisCaseSubjectToLRROrSRRSchemes " + IsThisCaseSubjectToLRROrSRRSchemes);
//            System.out.println("IsThisCaseSubjectToLRROrSRRSchemes > 4 || IsThisCaseSubjectToLRROrSRRSchemes < 1");
                throw new Exception("IsThisCaseSubjectToLRROrSRRSchemes > 4 || IsThisCaseSubjectToLRROrSRRSchemes < 1");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyLocalReferenceRent = 0;
            } else {
                WeeklyLocalReferenceRent = Integer.valueOf(fields[n]);
            }
            n++; //60
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklySingleRoomRent = 0;
            } else {
                WeeklySingleRoomRent = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeelklyClaimRelatedRent = 0;
            } else {
                WeelklyClaimRelatedRent = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                RentOfficerDeterminationOfIneligibleCharges = 0;
            } else {
                RentOfficerDeterminationOfIneligibleCharges = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyMaximumRent = 0;
            } else {
                WeeklyMaximumRent = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TotalDeductionForMeals = 0;
            } else {
                TotalDeductionForMeals = Integer.valueOf(fields[n]);
            }
            n++; //65
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyAdditionalDiscretionaryPayment = 0;
            } else {
                WeeklyAdditionalDiscretionaryPayment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ThirteenOrFiftyTwoWeekProtectionApplies = 0;
            } else {
                ThirteenOrFiftyTwoWeekProtectionApplies = Integer.valueOf(fields[n]);
            }
            if (ThirteenOrFiftyTwoWeekProtectionApplies > 1 || ThirteenOrFiftyTwoWeekProtectionApplies < 0) {
//            System.out.println("ThirteenOrFiftyTwoWeekProtectionApplies " + ThirteenOrFiftyTwoWeekProtectionApplies);
//            System.out.println("ThirteenOrFiftyTwoWeekProtectionApplies > 1 || ThirteenOrFiftyTwoWeekProtectionApplies < 0");
                throw new Exception("ThirteenOrFiftyTwoWeekProtectionApplies > 1 || ThirteenOrFiftyTwoWeekProtectionApplies < 0");
            }
            n++;
            DateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsAssessedIncomeFigure = 0;
            } else {
                ClaimantsAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsAdjustedAssessedIncomeFigure = 0;
            } else {
                ClaimantsAdjustedAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
            n++; //70
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsTotalCapital = 0;
            } else {
                ClaimantsTotalCapital = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsGrossWeeklyIncomeFromEmployment = 0;
            } else {
                ClaimantsGrossWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsNetWeeklyIncomeFromEmployment = 0;
            } else {
                ClaimantsNetWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsGrossWeeklyIncomeFromSelfEmployment = 0;
            } else {
                ClaimantsGrossWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsNetWeeklyIncomeFromSelfEmployment = 0;
            } else {
                ClaimantsNetWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
            n++; //75
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsTotalAmountOfEarningsDisregarded = 0;
            } else {
                ClaimantsTotalAmountOfEarningsDisregarded = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = 0;
            } else {
                ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromAttendanceAllowance = 0;
            } else {
                ClaimantsIncomeFromAttendanceAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromBusinessStartUpAllowance = 0;
            } else {
                ClaimantsIncomeFromBusinessStartUpAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromChildBenefit = 0;
            } else {
                ClaimantsIncomeFromChildBenefit = Integer.valueOf(fields[n]);
            }
            n++; //80
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent = 0;
            } else {
                ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromPersonalPension = 0;
            } else {
                ClaimantsIncomeFromPersonalPension = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromSevereDisabilityAllowance = 0;
            } else {
                ClaimantsIncomeFromSevereDisabilityAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromMaternityAllowance = 0;
            } else {
                ClaimantsIncomeFromMaternityAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromContributionBasedJobSeekersAllowance = 0;
            } else {
                ClaimantsIncomeFromContributionBasedJobSeekersAllowance = Integer.valueOf(fields[n]);
            }
            n++; //85
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromStudentGrantLoan = 0;
            } else {
                ClaimantsIncomeFromStudentGrantLoan = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromSubTenants = 0;
            } else {
                ClaimantsIncomeFromSubTenants = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromBoarders = 0;
            } else {
                ClaimantsIncomeFromBoarders = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromTrainingForWorkCommunityAction = 0;
            } else {
                ClaimantsIncomeFromTrainingForWorkCommunityAction = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromIncapacityBenefitShortTermLower = 0;
            } else {
                ClaimantsIncomeFromIncapacityBenefitShortTermLower = Integer.valueOf(fields[n]);
            }
            n++; //90
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromIncapacityBenefitShortTermHigher = 0;
            } else {
                ClaimantsIncomeFromIncapacityBenefitShortTermHigher = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromIncapacityBenefitLongTerm = 0;
            } else {
                ClaimantsIncomeFromIncapacityBenefitLongTerm = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromNewDeal50PlusEmploymentCredit = 0;
            } else {
                ClaimantsIncomeFromNewDeal50PlusEmploymentCredit = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromNewTaxCredits = 0;
            } else {
                ClaimantsIncomeFromNewTaxCredits = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent = 0;
            } else {
                ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent = Integer.valueOf(fields[n]);
            }
            n++; //95
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent = 0;
            } else {
                ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromGovernemntTraining = 0;
            } else {
                ClaimantsIncomeFromGovernemntTraining = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit = 0;
            } else {
                ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromCarersAllowance = 0;
            } else {
                ClaimantsIncomeFromCarersAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromStatutoryMaternityPaternityPay = 0;
            } else {
                ClaimantsIncomeFromStatutoryMaternityPaternityPay = Integer.valueOf(fields[n]);
            }
            n++; //100
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = 0;
            } else {
                ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP = 0;
            } else {
                ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromWarMobilitySupplement = 0;
            } else {
                ClaimantsIncomeFromWarMobilitySupplement = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromWidowsWidowersPension = 0;
            } else {
                ClaimantsIncomeFromWidowsWidowersPension = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromBereavementAllowance = 0;
            } else {
                ClaimantsIncomeFromBereavementAllowance = Integer.valueOf(fields[n]);
            }
            n++; //105
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromWidowedParentsAllowance = 0;
            } else {
                ClaimantsIncomeFromWidowedParentsAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromYouthTrainingScheme = 0;
            } else {
                ClaimantsIncomeFromYouthTrainingScheme = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromStatuatorySickPay = 0;
            } else {
                ClaimantsIncomeFromStatuatorySickPay = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsOtherIncome = 0;
            } else {
                ClaimantsOtherIncome = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsTotalAmountOfIncomeDisregarded = 0;
            } else {
                ClaimantsTotalAmountOfIncomeDisregarded = Integer.valueOf(fields[n]);
            }
            n++; //110
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                FamilyPremiumAwarded = 0;
            } else {
                FamilyPremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (FamilyPremiumAwarded > 1 || FamilyPremiumAwarded < 0) {
//            System.out.println("FamilyPremiumAwarded " + FamilyPremiumAwarded);
//            System.out.println("FamilyPremiumAwarded > 1 || FamilyPremiumAwarded < 0");
                throw new Exception("FamilyPremiumAwarded > 1 || FamilyPremiumAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                FamilyLoneParentPremiumAwarded = 0;
            } else {
                FamilyLoneParentPremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (FamilyLoneParentPremiumAwarded > 1 || FamilyLoneParentPremiumAwarded < 0) {
//            System.out.println("FamilyLoneParentPremiumAwarded " + FamilyLoneParentPremiumAwarded);
//            System.out.println("FamilyLoneParentPremiumAwarded > 1 || FamilyLoneParentPremiumAwarded < 0");
                throw new Exception("FamilyLoneParentPremiumAwarded > 1 || FamilyLoneParentPremiumAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                DisabilityPremiumAwarded = 0;
            } else {
                DisabilityPremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (DisabilityPremiumAwarded > 1 || DisabilityPremiumAwarded < 0) {
//            System.out.println("DisabilityPremiumAwarded " + DisabilityPremiumAwarded);
//            System.out.println("DisabilityPremiumAwarded > 1 || DisabilityPremiumAwarded < 0");
                throw new Exception("DisabilityPremiumAwarded > 1 || DisabilityPremiumAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                SevereDisabilityPremiumAwarded = 0;
            } else {
                SevereDisabilityPremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (SevereDisabilityPremiumAwarded > 1 || SevereDisabilityPremiumAwarded < 0) {
//            System.out.println("SevereDisabilityPremiumAwarded " + SevereDisabilityPremiumAwarded);
//            System.out.println("SevereDisabilityPremiumAwarded > 1 || SevereDisabilityPremiumAwarded < 0");
                throw new Exception("SevereDisabilityPremiumAwarded > 1 || SevereDisabilityPremiumAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                DisabledChildPremiumAwarded = 0;
            } else {
                DisabledChildPremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (DisabledChildPremiumAwarded > 1 || DisabledChildPremiumAwarded < 0) {
//            System.out.println("DisabledChildPremiumAwarded " + DisabledChildPremiumAwarded);
//            System.out.println("DisabledChildPremiumAwarded > 1 || DisabiledChildPremiumAwarded < 0");
                throw new Exception("DisabledChildPremiumAwarded > 1 || DisabiledChildPremiumAwarded < 0");
            }
            n++; //115
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                CarePremiumAwarded = 0;
            } else {
                CarePremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (CarePremiumAwarded > 1 || CarePremiumAwarded < 0) {
//            System.out.println("CarePremiumAwarded " + CarePremiumAwarded);
//            System.out.println("CarePremiumAwarded > 1 || CarePremiumAwarded < 0");
                throw new Exception("CarePremiumAwarded > 1 || CarePremiumAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                EnhancedDisabilityPremiumAwarded = 0;
            } else {
                EnhancedDisabilityPremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (EnhancedDisabilityPremiumAwarded > 1 || EnhancedDisabilityPremiumAwarded < 0) {
//            System.out.println("EnhancedDisabilityPremiumAwarded " + EnhancedDisabilityPremiumAwarded);
//            System.out.println("EnhancedDisabilityPremiumAwarded > 1 || EnhancedDisabilityPremiumAwarded < 0");
                throw new Exception("EnhancedDisabilityPremiumAwarded > 1 || EnhancedDisabilityPremiumAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                BereavementPremiumAwarded = 0;
            } else {
                BereavementPremiumAwarded = Integer.valueOf(fields[n]);
            }
            if (BereavementPremiumAwarded > 1 || BereavementPremiumAwarded < 0) {
//            System.out.println("BereavementPremiumAwarded " + BereavementPremiumAwarded);
//            System.out.println("BereavementPremiumAwarded > 1 || BereavementPremiumAwarded < 0");
                throw new Exception("BereavementPremiumAwarded > 1 || BereavementPremiumAwarded < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnerFlag = 0;
            } else {
                PartnerFlag = Integer.valueOf(fields[n]);
            }
            if (PartnerFlag > 2 || PartnerFlag < 0) {
//            System.out.println("PartnerFlag " + PartnerFlag);
//            System.out.println("PartnerFlag > 2 || PartnerFlag < 0");
                throw new Exception("PartnerFlag > 2 || PartnerFlag < 0");
            }
            n++;
            PartnersStartDate = fields[n];
            n++; //120
            PartnersEndDate = fields[n];
            n++;
            PartnersNationalInsuranceNumber = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersStudentIndicator = "N";
            } else {
                PartnersStudentIndicator = "Y";
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersAssessedIncomeFigure = 0;
            } else {
                PartnersAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersAdjustedAssessedIncomeFigure = 0;
            } else {
                PartnersAdjustedAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
            n++; //125
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersGrossWeeklyIncomeFromEmployment = 0;
            } else {
                PartnersGrossWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersNetWeeklyIncomeFromEmployment = 0;
            } else {
                PartnersNetWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersGrossWeeklyIncomeFromSelfEmployment = 0;
            } else {
                PartnersGrossWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersNetWeeklyIncomeFromSelfEmployment = 0;
            } else {
                PartnersNetWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersTotalAmountOfEarningsDisregarded = 0;
            } else {
                PartnersTotalAmountOfEarningsDisregarded = Integer.valueOf(fields[n]);
            }
            n++; //130
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = 0;
            } else {
                PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromAttendanceAllowance = 0;
            } else {
                PartnersIncomeFromAttendanceAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromBusinessStartUpAllowance = 0;
            } else {
                PartnersIncomeFromBusinessStartUpAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromChildBenefit = 0;
            } else {
                PartnersIncomeFromChildBenefit = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromPersonalPension = 0;
            } else {
                PartnersIncomeFromPersonalPension = Integer.valueOf(fields[n]);
            }
            n++; //135
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromSevereDisabilityAllowance = 0;
            } else {
                PartnersIncomeFromSevereDisabilityAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromMaternityAllowance = 0;
            } else {
                PartnersIncomeFromMaternityAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromContributionBasedJobSeekersAllowance = 0;
            } else {
                PartnersIncomeFromContributionBasedJobSeekersAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromStudentGrantLoan = 0;
            } else {
                PartnersIncomeFromStudentGrantLoan = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromSubTenants = 0;
            } else {
                PartnersIncomeFromSubTenants = Integer.valueOf(fields[n]);
            }
            n++; //140
            if (fields[140].equalsIgnoreCase("")) {
                PartnersIncomeFromBoarders = 0;
            } else {
                PartnersIncomeFromBoarders = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromTrainingForWorkCommunityAction = 0;
            } else {
                PartnersIncomeFromTrainingForWorkCommunityAction = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromIncapacityBenefitShortTermLower = 0;
            } else {
                PartnersIncomeFromIncapacityBenefitShortTermLower = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromIncapacityBenefitShortTermHigher = 0;
            } else {
                PartnersIncomeFromIncapacityBenefitShortTermHigher = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromIncapacityBenefitLongTerm = 0;
            } else {
                PartnersIncomeFromIncapacityBenefitLongTerm = Integer.valueOf(fields[n]);
            }
            n++; //145
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromNewDeal50PlusEmploymentCredit = 0;
            } else {
                PartnersIncomeFromNewDeal50PlusEmploymentCredit = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromNewTaxCredits = 0;
            } else {
                PartnersIncomeFromNewTaxCredits = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromDisabilityLivingAllowanceCareComponent = 0;
            } else {
                PartnersIncomeFromDisabilityLivingAllowanceCareComponent = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent = 0;
            } else {
                PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromGovernemntTraining = 0;
            } else {
                PartnersIncomeFromGovernemntTraining = Integer.valueOf(fields[n]);
            }
            n++; //150
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromIndustrialInjuriesDisablementBenefit = 0;
            } else {
                PartnersIncomeFromIndustrialInjuriesDisablementBenefit = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromCarersAllowance = 0;
            } else {
                PartnersIncomeFromCarersAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromStatuatorySickPay = 0;
            } else {
                PartnersIncomeFromStatuatorySickPay = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromStatutoryMaternityPaternityPay = 0;
            } else {
                PartnersIncomeFromStatutoryMaternityPaternityPay = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = 0;
            } else {
                PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = Integer.valueOf(fields[n]);
            }
            n++; //155
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromWarDisablementPensionArmedForcesGIP = 0;
            } else {
                PartnersIncomeFromWarDisablementPensionArmedForcesGIP = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromWarMobilitySupplement = 0;
            } else {
                PartnersIncomeFromWarMobilitySupplement = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromWidowsWidowersPension = 0;
            } else {
                PartnersIncomeFromWidowsWidowersPension = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromBereavementAllowance = 0;
            } else {
                PartnersIncomeFromBereavementAllowance = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromWidowedParentsAllowance = 0;
            } else {
                PartnersIncomeFromWidowedParentsAllowance = Integer.valueOf(fields[n]);
            }
            n++; //160
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromYouthTrainingScheme = 0;
            } else {
                PartnersIncomeFromYouthTrainingScheme = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersOtherIncome = 0;
            } else {
                PartnersOtherIncome = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersTotalAmountOfIncomeDisregarded = 0;
            } else {
                PartnersTotalAmountOfIncomeDisregarded = Integer.valueOf(fields[n]);
            }
            n++;
            DateOverPaymentDetectionActivityInitiatedOnCase = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                MethodOfOverpayentDetectionActivity = 0;
            } else {
                MethodOfOverpayentDetectionActivity = Integer.valueOf(fields[n]);
            }
            if (MethodOfOverpayentDetectionActivity > 99 || MethodOfOverpayentDetectionActivity < 0) {
//            System.out.println("MethodOfOverpayentDetectionActivity " + MethodOfOverpayentDetectionActivity);
//            System.out.println("MethodOfOverpayentDetectionActivity > 99 || MethodOfOverpayentDetectionActivity < 0");
                throw new Exception("MethodOfOverpayentDetectionActivity > 99 || MethodOfOverpayentDetectionActivity < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ReasonForOverpaymentDetectionActivity = 0;
            } else {
                ReasonForOverpaymentDetectionActivity = Integer.valueOf(fields[n]);
            }
            if (ReasonForOverpaymentDetectionActivity > 99 || ReasonForOverpaymentDetectionActivity < 0) {
//            System.out.println("ReasonForOverpaymentDetectionActivity " + ReasonForOverpaymentDetectionActivity);
//            System.out.println("ReasonForOverpaymentDetectionActivity > 99 || ReasonForOverpaymentDetectionActivity < 0");
                throw new Exception("ReasonForOverpaymentDetectionActivity > 99 || ReasonForOverpaymentDetectionActivity < 0");
            }
            n++;
            DoesTheOverpaymentDetectionActivityConstituteAFullReview = fields[n];
            n++;
            DateOverPaymentDetectionActivityIsCompleted = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                OutcomeOfOverPaymentDetectionActivity = 0;
            } else {
                OutcomeOfOverPaymentDetectionActivity = Integer.valueOf(fields[n]);
            }
            if (OutcomeOfOverPaymentDetectionActivity > 99 || OutcomeOfOverPaymentDetectionActivity < 0) {
//            System.out.println("OutcomeOfOverPaymentDetectionActivity " + OutcomeOfOverPaymentDetectionActivity);
//            System.out.println("OutcomeOfOverPaymentDetectionActivity > 99 || OutcomeOfOverPaymentDetectionActivity < 0");
                throw new Exception("OutcomeOfOverPaymentDetectionActivity > 99 || OutcomeOfOverPaymentDetectionActivity < 0");
            }
            n++;
            ClaimantsGender = fields[n];
            n++; //170
            PartnersDateOfBirth = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                RentAllowanceMethodOfPayment = 0;
            } else {
                RentAllowanceMethodOfPayment = Integer.valueOf(fields[n]);
            }
            if (RentAllowanceMethodOfPayment > 99 || RentAllowanceMethodOfPayment < 0) {
//            System.out.println("RentAllowanceMethodOfPayment " + RentAllowanceMethodOfPayment);
//            System.out.println("RentAllowanceMethodOfPayment > 99 || RentAllowanceMethodOfPayment < 0");
                throw new Exception("RentAllowanceMethodOfPayment > 99 || RentAllowanceMethodOfPayment < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                RentAllowancePaymentDestination = 0;
            } else {
                RentAllowancePaymentDestination = Integer.valueOf(fields[n]);
            }
            if (RentAllowancePaymentDestination > 99 || RentAllowancePaymentDestination < 0) {
//            System.out.println("RentAllowancePaymentDestination " + RentAllowancePaymentDestination);
//            System.out.println("RentAllowancePaymentDestination > 99 || RentAllowancePaymentDestination < 0");
                throw new Exception("RentAllowancePaymentDestination > 99 || RentAllowancePaymentDestination < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ContractualRentAmount = 0;
            } else {
                ContractualRentAmount = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TimePeriodContractualRentFigureCovers = 0;
            } else {
                TimePeriodContractualRentFigureCovers = Integer.valueOf(fields[n]);
            }
            if (TimePeriodContractualRentFigureCovers > 99 || TimePeriodContractualRentFigureCovers < 0) {
//            System.out.println("TimePeriodContractualRentFigureCovers " + TimePeriodContractualRentFigureCovers);
//            System.out.println("TimePeriodContractualRentFigureCovers > 99 || TimePeriodContractualRentFigureCovers < 0");
                throw new Exception("TimePeriodContractualRentFigureCovers > 99 || TimePeriodContractualRentFigureCovers < 0");
            }
            n++; //175
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromPensionCreditSavingsCredit = 0;
            } else {
                ClaimantsIncomeFromPensionCreditSavingsCredit = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromPensionCreditSavingsCredit = 0;
            } else {
                PartnersIncomeFromPensionCreditSavingsCredit = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromMaintenancePayments = 0;
            } else {
                ClaimantsIncomeFromMaintenancePayments = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromMaintenancePayments = 0;
            } else {
                PartnersIncomeFromMaintenancePayments = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromOccupationalPension = 0;
            } else {
                ClaimantsIncomeFromOccupationalPension = Integer.valueOf(fields[n]);
            }
            n++; //180
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromOccupationalPension = 0;
            } else {
                PartnersIncomeFromOccupationalPension = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsIncomeFromWidowsBenefit = 0;
            } else {
                ClaimantsIncomeFromWidowsBenefit = Integer.valueOf(fields[n]);
            }
            n++; //182
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersIncomeFromWidowsBenefit = 0;
            } else {
                PartnersIncomeFromWidowsBenefit = Integer.valueOf(fields[n]);
            }
            n = 193; //193
            CTBClaimEntitlementStartDate = fields[n];
            n++;
            DateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective = fields[n];
            n++; //195
            DateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TotalNumberOfRooms = 0;
            } else {
                TotalNumberOfRooms = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NonSelfContainedAccomodation = 0;
            } else {
                NonSelfContainedAccomodation = Integer.valueOf(fields[n]);
            }
            if (NonSelfContainedAccomodation > 1 || NonSelfContainedAccomodation < 0) {
//            System.out.println("NonSelfContainedAccomodation " + NonSelfContainedAccomodation);
//            System.out.println("NonSelfContainedAccomodation > 1 || NonSelfContainedAccomodation < 0");
                throw new Exception("NonSelfContainedAccomodation > 1 || NonSelfContainedAccomodation < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TypeOfLHANumberOfRoomsEntitedTo = 0;
            } else {
                TypeOfLHANumberOfRoomsEntitedTo = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TransitionalProtectionFormNationalRolloutOfLHA = 0;
            } else {
                TransitionalProtectionFormNationalRolloutOfLHA = Integer.valueOf(fields[n]);
            }
            if (TransitionalProtectionFormNationalRolloutOfLHA > 1 || TransitionalProtectionFormNationalRolloutOfLHA < 0) {
//            System.out.println("TransitionalProtectionFormNationalRolloutOfLHA " + TransitionalProtectionFormNationalRolloutOfLHA);
//            System.out.println("TransitionalProtectionFormNationalRolloutOfLHA > 1 || TransitionalProtectionFormNationalRolloutOfLHA < 0");
                throw new Exception("TransitionalProtectionFormNationalRolloutOfLHA > 1 || TransitionalProtectionFormNationalRolloutOfLHA < 0");
            }
            n++; //200
            Locality = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ValueOfLHA = 0;
            } else {
                ValueOfLHA = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective = 0;
            } else {
                ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective = Integer.valueOf(fields[n]);
            }
            if (ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0) {
//            System.out.println("ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective " + ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective);
//            System.out.println("ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0");
                throw new Exception("ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective = 0;
            } else {
                ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective = Integer.valueOf(fields[n]);
            }
            if (ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0) {
//            System.out.println("ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective " + ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective);
//            System.out.println("ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0");
                throw new Exception("ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0");
            }
            n++;
            PartnersGender = fields[n];
            n++; //205
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NonDependantGrossWeeklyIncomeFromRemunerativeWork = 0;
            } else {
                NonDependantGrossWeeklyIncomeFromRemunerativeWork = Integer.valueOf(fields[n]);
            }
            n = 211;
            HBClaimTreatAsDateMade = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                SourceOfMostRecentHBClaim = 0;
            } else {
                SourceOfMostRecentHBClaim = Integer.valueOf(fields[n]);
            }
            if (SourceOfMostRecentHBClaim > 99 || SourceOfMostRecentHBClaim < 0) {
//            System.out.println("SourceOfMostRecentHBClaim " + SourceOfMostRecentHBClaim);
//            System.out.println("SourceOfMostRecentHBClaim > 99 || SourceOfMostRecentHBClaim < 0");
                throw new Exception("SourceOfMostRecentHBClaim > 99 || SourceOfMostRecentHBClaim < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim = 0;
            } else {
                DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim = Integer.valueOf(fields[n]);
            }
            if (DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim > 1 || DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim < 0) {
//            System.out.println("DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim " + DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim);
//            System.out.println("DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim > 1 || DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim < 0");
                throw new Exception("DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim > 1 || DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim < 0");
            }
            n++;
            DateOfFirstHBPaymentRentAllowanceOnly = fields[n];
            n++; //215
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly = 0;
            } else {
                WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly = Integer.valueOf(fields[n]);
            }
            if (WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly > 1 || WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly < 0) {
//            System.out.println("WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly " + WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly);
//            System.out.println("WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly > 1 || WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly < 0");
                throw new Exception("WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly > 1 || WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WasThereABackdatedAwardMadeOnTheHBClaim = 0;
            } else {
                WasThereABackdatedAwardMadeOnTheHBClaim = Integer.valueOf(fields[n]);
            }
            if (WasThereABackdatedAwardMadeOnTheHBClaim > 1 || WasThereABackdatedAwardMadeOnTheHBClaim < 0) {
//            System.out.println("WasThereABackdatedAwardMadeOnTheHBClaim " + WasThereABackdatedAwardMadeOnTheHBClaim);
//            System.out.println("WasThereABackdatedAwardMadeOnTheHBClaim > 1 || WasThereABackdatedAwardMadeOnTheHBClaim < 0");
                throw new Exception("WasThereABackdatedAwardMadeOnTheHBClaim > 1 || WasThereABackdatedAwardMadeOnTheHBClaim < 0");
            }
            n++;
            DateHBBackdatingFrom = fields[n];
            n++;
            DateHBBackdatingTo = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TotalAmountOfBackdatedHBAwarded = 0;
            } else {
                TotalAmountOfBackdatedHBAwarded = Integer.valueOf(fields[n]);
            }
            n++; //220
            CTBClaimTreatAsMadeDate = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                SourceOfTheMostRecentCTBClaim = 0;
            } else {
                SourceOfTheMostRecentCTBClaim = Integer.valueOf(fields[n]);
            }
            if (SourceOfTheMostRecentCTBClaim > 99 || SourceOfTheMostRecentCTBClaim < 0) {
//            System.out.println("SourceOfTheMostRecentCTBClaim " + SourceOfTheMostRecentCTBClaim);
//            System.out.println("SourceOfTheMostRecentCTBClaim > 99 || SourceOfTheMostRecentCTBClaim < 0");
                throw new Exception("SourceOfTheMostRecentCTBClaim > 99 || SourceOfTheMostRecentCTBClaim < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim = 0;
            } else {
                DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim = Integer.valueOf(fields[n]);
            }
            if (DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim > 1 || DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim < 0) {
//            System.out.println("DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim " + DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim);
//            System.out.println("DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim > 10 || DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim < 0");
                throw new Exception("DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim > 10 || DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WasThereABackdatedAwardMadeOnTheCTBClaim = 0;
            } else {
                WasThereABackdatedAwardMadeOnTheCTBClaim = Integer.valueOf(fields[n]);
            }
//        if (WasThereABackdatedAwardMadeOnTheCTBClaim > 1 || WasThereABackdatedAwardMadeOnTheCTBClaim < 0) {
////            System.out.println("WasThereABackdatedAwardMadeOnTheCTBClaim " + WasThereABackdatedAwardMadeOnTheCTBClaim);
////            System.out.println("WasThereABackdatedAwardMadeOnTheCTBClaim > 1 || WasThereABackdatedAwardMadeOnTheCTBClaim < 0");
//            throw new Exception("WasThereABackdatedAwardMadeOnTheCTBClaim > 1 || WasThereABackdatedAwardMadeOnTheCTBClaim < 0");
//        }
            n++;
            DateCTBBackdatingFrom = fields[n];
            n++; //225
            DateCTBBackdatingTo = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TotalAmountOfBackdatedCTBAwarded = 0;
            } else {
                TotalAmountOfBackdatedCTBAwarded = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly = 0;
            } else {
                InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly = Integer.valueOf(fields[n]);
            }
//        if (InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly > 1 || InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly < 0) {
////            System.out.println("InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly " + InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly);
////            System.out.println("InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly > 1 || InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly < 0");
//            throw new Exception("InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly > 1 || InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly < 0");
//        }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation = 0;
            } else {
                IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation = Integer.valueOf(fields[n]);
            }
//        if (IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation > 99 || IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation < 0) {
////            System.out.println("IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation " + IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation);
////            System.out.println("IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation > 99 || IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation < 0");
//            throw new Exception("IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation > 99 || IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation < 0");
//        }
            n++;
            DateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT = fields[n];
            n++; //230
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                PartnersTotalCapital = 0;
            } else {
                PartnersTotalCapital = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure = 0;
            } else {
                WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsTotalHoursOfRemunerativeWorkPerWeek = 0;
            } else {
                try {
                    ClaimantsTotalHoursOfRemunerativeWorkPerWeek = Double.valueOf(fields[n]);
                } catch (NumberFormatException e) {
                    e.printStackTrace(System.err);
                    throw e;
                }
            }
            n++;
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
            n = 236;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TotalHBPaymentsCreditsSinceLastExtract = 0;
            } else {
                TotalHBPaymentsCreditsSinceLastExtract = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TotalCTBPaymentsCreditsSinceLastExtract = 0;
            } else {
                TotalCTBPaymentsCreditsSinceLastExtract = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ClaimantsEthnicGroup = 0;
            } else {
                ClaimantsEthnicGroup = Integer.valueOf(fields[n]);
            }
            if (ClaimantsEthnicGroup > 99 || ClaimantsEthnicGroup < 0) {
//            System.out.println("ClaimantsEthnicGroup " + ClaimantsEthnicGroup);
//            System.out.println("ClaimantsEthnicGroup > 99 || ClaimantsEthnicGroup < 0");
                throw new Exception("ClaimantsEthnicGroup > 99 || ClaimantsEthnicGroup < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NewWeeklyHBEntitlementAfterTheChange = 0;
            } else {
                NewWeeklyHBEntitlementAfterTheChange = Integer.valueOf(fields[n]);
            }
            n++; //240
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NewWeeklyCTBEntitlementAfterTheChange = 0;
            } else {
                NewWeeklyCTBEntitlementAfterTheChange = Integer.valueOf(fields[n]);
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                TypeOfChange = 0;
            } else {
                TypeOfChange = Integer.valueOf(fields[n]);
            }
            if (TypeOfChange > 2 || TypeOfChange < 0) {
//            System.out.println("TypeOfChange " + TypeOfChange);
//            System.out.println("TypeOfChange > 2 || TypeOfChange < 0");
                throw new Exception("TypeOfChange > 2 || TypeOfChange < 0");
            }
            n++;
            DateLAFirstNotifiedOfChangeInClaimDetails = fields[n];
            n += 2;
            DateChangeOfDetailsAreEffectiveFrom = fields[n];
            n++; //245
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                IfNotAnnualUpratingHowWasTheChangeIdentified = 0;
            } else {
                IfNotAnnualUpratingHowWasTheChangeIdentified = Integer.valueOf(fields[n]);
            }
            if (IfNotAnnualUpratingHowWasTheChangeIdentified > 99 || IfNotAnnualUpratingHowWasTheChangeIdentified < 0) {
//            System.out.println("IfNotAnnualUpratingHowWasTheChangeIdentified " + IfNotAnnualUpratingHowWasTheChangeIdentified);
//            System.out.println("IfNotAnnualUpratingHowWasTheChangeIdentified > 99 || IfNotAnnualUpratingHowWasTheChangeIdentified < 0");
                throw new Exception("IfNotAnnualUpratingHowWasTheChangeIdentified > 99 || IfNotAnnualUpratingHowWasTheChangeIdentified < 0");
            }
            n++;
            DateSupercessionDecisionWasMadeOnTheHBClaim = fields[n];
            n++;
            DateSupercessionDecisionWasMadeOnTheCTBClaim = fields[n];
            n += 3; //250
            DateApplicationForRevisionReconsiderationReceived = fields[n];
            n++;
            DateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration = fields[n];
            n++;
            DateAppealApplicationWasLodged = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                OutcomeOfAppealApplication = 0;
            } else {
                OutcomeOfAppealApplication = Integer.valueOf(fields[n]);
            }
            if (OutcomeOfAppealApplication > 99 || OutcomeOfAppealApplication < 0) {
//            System.out.println("OutcomeOfAppealApplication " + OutcomeOfAppealApplication);
//            System.out.println("OutcomeOfAppealApplication > 99 || OutcomeOfAppealApplication < 0");
                throw new Exception("OutcomeOfAppealApplication > 99 || OutcomeOfAppealApplication < 0");
            }
            n++; //254
            DateOfOutcomeOfAppealApplication = fields[n];
            n += 6; //260
            DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim = fields[n];
            n++;
            DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim = fields[n];
            n++;
            DateCouncilTaxPayable = fields[n];
            n++;
            DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim = fields[n];
            n++;
            DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim = fields[n];
            n++; //265
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                SoftwareProvider = 0;
            } else {
                SoftwareProvider = Integer.valueOf(fields[n]);
            }
            if (SoftwareProvider > 3 || SoftwareProvider < 0) {
//            System.out.println("SoftwareProvider " + SoftwareProvider);
//            System.out.println("SoftwareProvider > 3 || SoftwareProvider < 0");
                throw new Exception("SoftwareProvider > 3 || SoftwareProvider < 0");
            }
            n++;
            StaffingFTE = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ContractingOutHandlingAndMaintenanceOfHBCTB = 0;
            } else {
                ContractingOutHandlingAndMaintenanceOfHBCTB = Integer.valueOf(fields[n]);
            }
            if (ContractingOutHandlingAndMaintenanceOfHBCTB > 9 || ContractingOutHandlingAndMaintenanceOfHBCTB < 0) {
//            System.out.println("ContractingOutHandlingAndMaintenanceOfHBCTB " + ContractingOutHandlingAndMaintenanceOfHBCTB);
//            System.out.println("ContractingOutHandlingAndMaintenanceOfHBCTB > 9 || ContractingOutHandlingAndMaintenanceOfHBCTB < 0");
                throw new Exception("ContractingOutHandlingAndMaintenanceOfHBCTB > 9 || ContractingOutHandlingAndMaintenanceOfHBCTB < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                ContractingOutCounterFraudWorkRelatingToHBCTB = 0;
            } else {
                ContractingOutCounterFraudWorkRelatingToHBCTB = Integer.valueOf(fields[n]);
            }
            if (ContractingOutCounterFraudWorkRelatingToHBCTB > 9 || ContractingOutCounterFraudWorkRelatingToHBCTB < 0) {
//            System.out.println("ContractingOutCounterFraudWorkRelatingToHBCTB " + ContractingOutCounterFraudWorkRelatingToHBCTB);
//            System.out.println("ContractingOutCounterFraudWorkRelatingToHBCTB > 9 || ContractingOutCounterFraudWorkRelatingToHBCTB < 0");
                throw new Exception("ContractingOutCounterFraudWorkRelatingToHBCTB > 9 || ContractingOutCounterFraudWorkRelatingToHBCTB < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                NumberOfBedroomsForLHARolloutCasesOnly = 0;
            } else {
                NumberOfBedroomsForLHARolloutCasesOnly = Integer.valueOf(fields[n]);
            }
            n++; //270
            PartnersDateOfDeath = fields[n];
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                JointTenancyFlag = 0;
            } else {
                JointTenancyFlag = Integer.valueOf(fields[n]);
            }
            if (JointTenancyFlag > 2 || JointTenancyFlag < 0) {
//            System.out.println("JointTenancyFlag " + JointTenancyFlag);
//            System.out.println("JointTenancyFlag > 2 || JointTenancyFlag < 0");
                throw new Exception("JointTenancyFlag > 2 || JointTenancyFlag < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                AppointeeFlag = 0;
            } else {
                AppointeeFlag = Integer.valueOf(fields[n]);
            }
            if (AppointeeFlag > 2 || AppointeeFlag < 0) {
//            System.out.println("AppointeeFlag " + AppointeeFlag);
//            System.out.println("AppointeeFlag > 2 || AppointeeFlag < 0");
                throw new Exception("AppointeeFlag > 2 || AppointeeFlag < 0");
            }
            n++;
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                RentFreeWeeksIndicator = 0;
            } else {
                RentFreeWeeksIndicator = Integer.valueOf(fields[n]);
            }
            n++; //274
            if (fields.length > n) {
                LastPaidToDate = fields[n];
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0;
                } else {
                    WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = Integer.valueOf(fields[n]);
                }
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    ClaimantsWeeklyIncomeFromESABasicElement = 0;
                } else {
                    ClaimantsWeeklyIncomeFromESABasicElement = Integer.valueOf(fields[n]);
                }
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    PartnersWeeklyIncomeFromESABasicElement = 0;
                } else {
                    PartnersWeeklyIncomeFromESABasicElement = Integer.valueOf(fields[n]);
                }
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    ClaimantsWeeklyIncomeFromESAWRAGElement = 0;
                } else {
                    ClaimantsWeeklyIncomeFromESAWRAGElement = Integer.valueOf(fields[n]);
                }
            }
            n++; //279
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    PartnersWeeklyIncomeFromESAWRAGElement = 0;
                } else {
                    PartnersWeeklyIncomeFromESAWRAGElement = Integer.valueOf(fields[n]);
                }
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    ClaimantsWeeklyIncomeFromESASCGElement = 0;
                } else {
                    ClaimantsWeeklyIncomeFromESASCGElement = Integer.valueOf(fields[n]);
                }
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    PartnersWeeklyIncomeFromESASCGElement = 0;
                } else {
                    PartnersWeeklyIncomeFromESASCGElement = Integer.valueOf(fields[n]);
                }
            }
            n++;
            if (fields.length > n) {
                WRAGPremiumFlag = fields[n];
            }
            n++; //283
            if (fields.length > n) {
                SCGPremiumFlag = fields[n];
            }
            if (type == 1) {
                n++;
                if (fields.length > n) {
                    LandlordPostcode = fields[n];
                }
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    SubRecordType = 0;
                } else {
                    try {
                        SubRecordType = Integer.valueOf(fields[n]);
                    } catch (NumberFormatException e) {
                        System.err.println("RecordID " + RecordID + " SubRecordType " + SubRecordType + " not converted to Integer");
                        e.printStackTrace(System.err);
                        throw e;
                    }
                }
                if (SubRecordType > 2 || SubRecordType < 0) {
//                System.out.println("SubRecordType " + SubRecordType);
//                System.out.println("SubRecordType > 2 || SubRecordType < 0");
                    throw new Exception("SubRecordType > 2 || SubRecordType < 0");
                }
            }
            if (type == 1) {
                n++;
                if (fields.length > n) {
                    SubRecordDateOfBirth = fields[n];
                }
            }
            n++;
            if (fields.length > n) {
                SubRecordChildReferenceNumberOrNINO = fields[n];
            }
            n++;
            if (fields.length > n) {
                SubRecordStartDate = fields[n];
            }
            n++;
            if (fields.length > n) {
                SubRecordEndDate = fields[n];
            }
            if (type == 1) {
                n++;
                if (fields.length > n) {
                    SubRecordDateOfBirth = fields[n];
                }
            }
            n++;
            if (fields.length > n) {
                IfThisActivityResolvesAnHBMSReferralProvideRMSNumber = fields[n];
            }
            n++;
            if (fields.length > n) {
                HBMSRuleScanID = fields[n];
            }
            n++;
            if (fields.length > n) {
                DateOfHBMSMatch = fields[n];
            }
            n++;
            if (fields.length > n) {
                IfResolutionOfHBMSReferralDoesNotResultInAFinancialAdjustmentPleaseIndicateTheReasonWhy = fields[n];
            }
            n++;
            if (fields.length > n) {
                UniqueTRecordIdentifier = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonCapital = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonClaimentPartnersEarnedIncome = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonNonDependentsEarnedIncome = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonPassportingStatus = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonIncomeFromDWPBenefits = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonTaxCredits = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonOtherIncome = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonLivingTogetherAsHusbandAndWife = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonNumberOfNonDependents = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonNumberOfDependents = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonNonResidence = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonEligibleRentCouncilTax = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonIneligible = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonIdentityDeath = fields[n];
            }
            n++;
            if (fields.length > n) {
                OverpaymentReasonOther = fields[n];
            }
            n++;
            if (fields.length > n) {
                BenefitThatThisPaymentErrorRelatesTo = fields[n];
            }
            n++;
            if (fields.length > n) {
                if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ")) {
                    TotalValueOfPaymentError = 0;
                } else {
                    try {
                        TotalValueOfPaymentError = Integer.valueOf(fields[n]);
                    } catch (NumberFormatException e) {
                        e.printStackTrace(System.err);
                        throw e;
                    }
                }
            }
            n++;
            if (fields.length > n) {
                WeeklyBenefitDiscrepancyAtStartOfPaymentError = fields[n];
            }
            n++;
            if (fields.length > n) {
                StartDateOfPaymentErrorPeriod = fields[n];
            }
            n++;
            if (fields.length > n) {
                EndDateOfPaymentErrorPeriod = fields[n];
            }
            n++;
            if (fields.length > n) {
                WhatWasTheCauseOfTheOverPayment = fields[n];
            }
        }
    }

    @Override
    public String toString() {
        return "RecordID " + RecordID
                + ",RecordType " + RecordType
                + ",HousingBenefitClaimReferenceNumber " + HousingBenefitClaimReferenceNumber
                + ",HousingBenefitClaimReferenceNumber " + HousingBenefitClaimReferenceNumber
                + ",CouncilTaxBenefitClaimReferenceNumber " + CouncilTaxBenefitClaimReferenceNumber
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
                + ",WeelklyClaimRelatedRent " + WeelklyClaimRelatedRent
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
                + ",TransitionalProtectionFormNationalRolloutOfLHA " + TransitionalProtectionFormNationalRolloutOfLHA
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
     * @return the WeelklyClaimRelatedRent
     */
    public int getWeelklyClaimRelatedRent() {
        return WeelklyClaimRelatedRent;
    }

    /**
     * @param WeelklyClaimRelatedRent the WeelklyClaimRelatedRent to set
     */
    public void setWeelklyClaimRelatedRent(int WeelklyClaimRelatedRent) {
        this.WeelklyClaimRelatedRent = WeelklyClaimRelatedRent;
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
     * @return the TransitionalProtectionFormNationalRolloutOfLHA
     */
    public int getTransitionalProtectionFormNationalRolloutOfLHA() {
        return TransitionalProtectionFormNationalRolloutOfLHA;
    }

    /**
     * @param TransitionalProtectionFormNationalRolloutOfLHA the
     * TransitionalProtectionFormNationalRolloutOfLHA to set
     */
    public void setTransitionalProtectionFormNationalRolloutOfLHA(int TransitionalProtectionFormNationalRolloutOfLHA) {
        this.TransitionalProtectionFormNationalRolloutOfLHA = TransitionalProtectionFormNationalRolloutOfLHA;
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
    public HashSet<SHBE_DataRecord> getSRecords() {
        if (SRecords == null) {
            SRecords = new HashSet<SHBE_DataRecord>();
        }
        return SRecords;
    }

    /**
     * @param SRecords the SRecords to set
     */
    public void setSRecords(HashSet<SHBE_DataRecord> SRecords) {
        this.SRecords = SRecords;
    }

}
