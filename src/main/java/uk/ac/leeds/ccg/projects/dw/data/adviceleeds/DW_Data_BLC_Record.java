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
package uk.ac.leeds.ccg.projects.dw.data.adviceleeds;

import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.io.DW_IO;

/**
 *
 * @author geoagdt
 */
public class DW_Data_BLC_Record extends DW_Data_Postcode_Record {

    // Header
    //Client Reference,Does the client agree to be contact for evaluation / market research?,Client's first name,Client's second name,1st Line of Address,2nd Line of Address,Town/City,Postcode,Telephone Number,Email address,Date of first contact,Date case is closed,1. Gender,2. Age,3. Ethnicity,4. State Benefits (Excluding child benefit),5. Housing Tenure,6. Marital Status,7. Employment Status,8. Total amount of debts,A. Referred to telephone provider for debt solution,B. Self help,C. DRO,D. Bankruptcy,E. Referred to IVA provider,F. CASHflow,G. Client represented in Court,H. Negotiation with Creditors by Debt Adviser,I. Income maximisation,J. Other,A. No Bank Account                                                                                                                                                                                                      (NONE = No Bank Account /                                   A/C = Account is held),B. No Savings Held                                                                                                                                                             (NONE = No Savings Held /                                              SAV = Savings Held),C. User of High Interest Credit,D. Priority Debts Owed,"E. Individual Income < �14,500","F. Household Income < �15,600",G. No Home Contents Insurance,H. First Half of Postcode plus one digit,Number of Financial Exclusion Indicators,11. Initial channel used to deliver debt advice,12. Type of Advice,13. Agreeable to Evaluation,,,,,,,,,,,,,,,,,
    // Example record
    //434,Yes, , ,,,,,,,02/04/2012,03/04/2012,Female,D. 35-49,A. White: British,Yes,B. Buying Home (mortgage etc.),B. Married /cohabiting/civil partnership,C. Part-Time,35855,No ,Yes,No ,No ,No ,No ,No ,No ,No ,No ,A/C,None,No ,Yes,Yes,No ,No ,LS10 4,3,A. Face to Face,B. One-off,Yes,,,Male,,Yes,A. Own outright,A. Face to Face,,,,,,,,,,
    // Somehow the formula in the final line of the cell adds the following to the csv for the example record shown: ",,,Male,,Yes,A. Own outright,A. Face to Face,,,,,,,,,,"
    // The additional part of the line is ignored for the time being.
    
    private String ClientReference;
    private String DoesTheClientAgreeToBeContactForEvaluationOrMarketResearch;
    private String ClientFirstName;
    private String ClientSecondName;
    private String FirstLineOfAddress;
    private String SecondLineOfAddress;
    private String TownOrCity;
//    private String Postcode;
    private String TelephoneNumber;
    private String EmailAddress;
    private String DateOfFirstContact;
    private String DateCaseClosed;
    private String Gender;
    private String Age;
    private String Ethnicity;
    private String StateBenefitsExcludingChildBenefit;
    private String HousingTenure;
    private String MaritalStatus;
    private String EmploymentStatus;
    private String TotalAmountOfDebts;
    private String ReferredToTelephoneProviderForDebtSolution;
    private String SelfHelp;
    private String DRO;
    private String Bankruptcy;
    private String ReferredToIVAProvider;
    private String CASHflow;
    private String ClientRepresentedInCourt;
    private String NegotiationWithCreditorsByDebtAdviser;
    private String IncomeMaximisation;
    private String Other;
    private String NoBankAccount; // (NONE = No Bank Account / A/C = Account is held)
    private String NoSavingsHeld; // (NONE = No Savings Held / SAV = Savings Held)
    private String UserOfHighInterestCredit;
    private String PriorityDebtsOwed;
    private String IndividualIncomeLT14500;
    private String HouseholdIncomeLT15600;
    private String NoHomeContentsInsurance;
    private String PostcodeSector;
    private String NumberOfFinancialExclusionIndicators;
    private String InitialChannelUsedToDeliverDebtAdvice;
    private String TypeOfAdvice;
    private String AgreeableToEvaluation;

    public DW_Data_BLC_Record(DW_Environment env) {
        super(env);
    }

    /**
     * Creates a null record in case this is needed
     *
     * @param env
     * @param RecordID
     */
    public DW_Data_BLC_Record(
            DW_Environment env,
            long RecordID) {
        super(env);
        setRecordID(RecordID);
    }

    /**
     * @param env
     * @param RecordID
     * @param line
     * @param handler
     * @throws java.lang.Exception
     */
    public DW_Data_BLC_Record(
            DW_Environment env,
            long RecordID,
            String line,
            DW_Data_BLC_Handler handler) throws Exception {
        this(env, RecordID);
        String[] fields;
        fields = DW_IO.splitWithQuotesThenCommas(line);
        int fieldCount = fields.length;
        if (fieldCount < 41) {
            System.out.println("RecordID " + RecordID + ", fieldCount" + fieldCount + " < 41!");
        } else {
            ClientReference = fields[0];
            DoesTheClientAgreeToBeContactForEvaluationOrMarketResearch = fields[1];
            ClientFirstName = fields[2];
            ClientSecondName = fields[3];
            FirstLineOfAddress = fields[4];
            SecondLineOfAddress = fields[5];
            TownOrCity = fields[6];
            setPostcode(fields[7]);
            TelephoneNumber = fields[8];
            EmailAddress = fields[9];
            DateOfFirstContact = fields[10];
            DateCaseClosed = fields[11];
            Gender = fields[12];
            Age = fields[13];
            Ethnicity = fields[14];
            StateBenefitsExcludingChildBenefit = fields[15];
            HousingTenure = fields[16];
            MaritalStatus = fields[17];
            EmploymentStatus = fields[18];
            TotalAmountOfDebts = fields[19];
            ReferredToTelephoneProviderForDebtSolution = fields[20];
            SelfHelp = fields[21];
            DRO = fields[22];
            Bankruptcy = fields[23];
            ReferredToIVAProvider = fields[24];
            CASHflow = fields[25];
            ClientRepresentedInCourt = fields[26];
            NegotiationWithCreditorsByDebtAdviser = fields[27];
            IncomeMaximisation = fields[28];
            Other = fields[29];
            NoBankAccount = fields[30];
            NoSavingsHeld = fields[31];
            UserOfHighInterestCredit = fields[32];
            PriorityDebtsOwed = fields[33];
            IndividualIncomeLT14500 = fields[34];
            HouseholdIncomeLT15600 = fields[35];
            NoHomeContentsInsurance = fields[36];
            PostcodeSector = fields[37];
            NumberOfFinancialExclusionIndicators = fields[38];
            InitialChannelUsedToDeliverDebtAdvice = fields[39];
            TypeOfAdvice = fields[40];
            AgreeableToEvaluation = fields[41];
        }
    }

    

    /**
     * @return the ClientReference
     */
    public String getClientReference() {
        return ClientReference;
    }

    /**
     * @param ClientReference the ClientReference to set
     */
    public void setClientReference(String ClientReference) {
        this.ClientReference = ClientReference;
    }

    /**
     * @return the DoesTheClientAgreeToBeContactForEvaluationOrMarketResearch
     */
    public String getDoesTheClientAgreeToBeContactForEvaluationOrMarketResearch() {
        return DoesTheClientAgreeToBeContactForEvaluationOrMarketResearch;
    }

    /**
     * @param DoesTheClientAgreeToBeContactForEvaluationOrMarketResearch the
     * DoesTheClientAgreeToBeContactForEvaluationOrMarketResearch to set
     */
    public void setDoesTheClientAgreeToBeContactForEvaluationOrMarketResearch(String DoesTheClientAgreeToBeContactForEvaluationOrMarketResearch) {
        this.DoesTheClientAgreeToBeContactForEvaluationOrMarketResearch = DoesTheClientAgreeToBeContactForEvaluationOrMarketResearch;
    }

    /**
     * @return the ClientFirstName
     */
    public String getClientFirstName() {
        return ClientFirstName;
    }

    /**
     * @param ClientFirstName the ClientFirstName to set
     */
    public void setClientFirstName(String ClientFirstName) {
        this.ClientFirstName = ClientFirstName;
    }

    /**
     * @return the ClientSecondName
     */
    public String getClientSecondName() {
        return ClientSecondName;
    }

    /**
     * @param ClientSecondName the ClientSecondName to set
     */
    public void setClientSecondName(String ClientSecondName) {
        this.ClientSecondName = ClientSecondName;
    }

    /**
     * @return the FirstLineOfAddress
     */
    public String getFirstLineOfAddress() {
        return FirstLineOfAddress;
    }

    /**
     * @param FirstLineOfAddress the FirstLineOfAddress to set
     */
    public void setFirstLineOfAddress(String FirstLineOfAddress) {
        this.FirstLineOfAddress = FirstLineOfAddress;
    }

    /**
     * @return the SecondLineOfAddress
     */
    public String getSecondLineOfAddress() {
        return SecondLineOfAddress;
    }

    /**
     * @param SecondLineOfAddress the SecondLineOfAddress to set
     */
    public void setSecondLineOfAddress(String SecondLineOfAddress) {
        this.SecondLineOfAddress = SecondLineOfAddress;
    }

    /**
     * @return the TownOrCity
     */
    public String getTownOrCity() {
        return TownOrCity;
    }

    /**
     * @param TownOrCity the TownOrCity to set
     */
    public void setTownOrCity(String TownOrCity) {
        this.TownOrCity = TownOrCity;
    }

    /**
     * @return the TelephoneNumber
     */
    public String getTelephoneNumber() {
        return TelephoneNumber;
    }

    /**
     * @param TelephoneNumber the TelephoneNumber to set
     */
    public void setTelephoneNumber(String TelephoneNumber) {
        this.TelephoneNumber = TelephoneNumber;
    }

    /**
     * @return the EmailAddress
     */
    public String getEmailAddress() {
        return EmailAddress;
    }

    /**
     * @param EmailAddress the EmailAddress to set
     */
    public void setEmailAddress(String EmailAddress) {
        this.EmailAddress = EmailAddress;
    }

    /**
     * @return the DateOfFirstContact
     */
    public String getDateOfFirstContact() {
        return DateOfFirstContact;
    }

    /**
     * @param DateOfFirstContact the DateOfFirstContact to set
     */
    public void setDateOfFirstContact(String DateOfFirstContact) {
        this.DateOfFirstContact = DateOfFirstContact;
    }

    /**
     * @return the DateCaseClosed
     */
    public String getDateCaseClosed() {
        return DateCaseClosed;
    }

    /**
     * @param DateCaseClosed the DateCaseClosed to set
     */
    public void setDateCaseClosed(String DateCaseClosed) {
        this.DateCaseClosed = DateCaseClosed;
    }

    /**
     * @return the Gender
     */
    public String getGender() {
        return Gender;
    }

    /**
     * @param Gender the Gender to set
     */
    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    /**
     * @return the Age
     */
    public String getAge() {
        return Age;
    }

    /**
     * @param Age the Age to set
     */
    public void setAge(String Age) {
        this.Age = Age;
    }

    /**
     * @return the Ethnicity
     */
    public String getEthnicity() {
        return Ethnicity;
    }

    /**
     * @param Ethnicity the Ethnicity to set
     */
    public void setEthnicity(String Ethnicity) {
        this.Ethnicity = Ethnicity;
    }

    /**
     * @return the StateBenefitsExcludingChildBenefit
     */
    public String getStateBenefitsExcludingChildBenefit() {
        return StateBenefitsExcludingChildBenefit;
    }

    /**
     * @param StateBenefitsExcludingChildBenefit the
     * StateBenefitsExcludingChildBenefit to set
     */
    public void setStateBenefitsExcludingChildBenefit(String StateBenefitsExcludingChildBenefit) {
        this.StateBenefitsExcludingChildBenefit = StateBenefitsExcludingChildBenefit;
    }

    /**
     * @return the HousingTenure
     */
    public String getHousingTenure() {
        return HousingTenure;
    }

    /**
     * @param HousingTenure the HousingTenure to set
     */
    public void setHousingTenure(String HousingTenure) {
        this.HousingTenure = HousingTenure;
    }

    /**
     * @return the MaritalStatus
     */
    public String getMaritalStatus() {
        return MaritalStatus;
    }

    /**
     * @param MaritalStatus the MaritalStatus to set
     */
    public void setMaritalStatus(String MaritalStatus) {
        this.MaritalStatus = MaritalStatus;
    }

    /**
     * @return the EmploymentStatus
     */
    public String getEmploymentStatus() {
        return EmploymentStatus;
    }

    /**
     * @param EmploymentStatus the EmploymentStatus to set
     */
    public void setEmploymentStatus(String EmploymentStatus) {
        this.EmploymentStatus = EmploymentStatus;
    }

    /**
     * @return the TotalAmountOfDebts
     */
    public String getTotalAmountOfDebts() {
        return TotalAmountOfDebts;
    }

    /**
     * @param TotalAmountOfDebts the TotalAmountOfDebts to set
     */
    public void setTotalAmountOfDebts(String TotalAmountOfDebts) {
        this.TotalAmountOfDebts = TotalAmountOfDebts;
    }

    /**
     * @return the ReferredToTelephoneProviderForDebtSolution
     */
    public String getReferredToTelephoneProviderForDebtSolution() {
        return ReferredToTelephoneProviderForDebtSolution;
    }

    /**
     * @param ReferredToTelephoneProviderForDebtSolution the
     * ReferredToTelephoneProviderForDebtSolution to set
     */
    public void setReferredToTelephoneProviderForDebtSolution(String ReferredToTelephoneProviderForDebtSolution) {
        this.ReferredToTelephoneProviderForDebtSolution = ReferredToTelephoneProviderForDebtSolution;
    }

    /**
     * @return the SelfHelp
     */
    public String getSelfHelp() {
        return SelfHelp;
    }

    /**
     * @param SelfHelp the SelfHelp to set
     */
    public void setSelfHelp(String SelfHelp) {
        this.SelfHelp = SelfHelp;
    }

    /**
     * @return the DRO
     */
    public String getDRO() {
        return DRO;
    }

    /**
     * @param DRO the DRO to set
     */
    public void setDRO(String DRO) {
        this.DRO = DRO;
    }

    /**
     * @return the Bankruptcy
     */
    public String getBankruptcy() {
        return Bankruptcy;
    }

    /**
     * @param Bankruptcy the Bankruptcy to set
     */
    public void setBankruptcy(String Bankruptcy) {
        this.Bankruptcy = Bankruptcy;
    }

    /**
     * @return the ReferredToIVAProvider
     */
    public String getReferredToIVAProvider() {
        return ReferredToIVAProvider;
    }

    /**
     * @param ReferredToIVAProvider the ReferredToIVAProvider to set
     */
    public void setReferredToIVAProvider(String ReferredToIVAProvider) {
        this.ReferredToIVAProvider = ReferredToIVAProvider;
    }

    /**
     * @return the CASHflow
     */
    public String getCASHflow() {
        return CASHflow;
    }

    /**
     * @param CASHflow the CASHflow to set
     */
    public void setCASHflow(String CASHflow) {
        this.CASHflow = CASHflow;
    }

    /**
     * @return the ClientRepresentedInCourt
     */
    public String getClientRepresentedInCourt() {
        return ClientRepresentedInCourt;
    }

    /**
     * @param ClientRepresentedInCourt the ClientRepresentedInCourt to set
     */
    public void setClientRepresentedInCourt(String ClientRepresentedInCourt) {
        this.ClientRepresentedInCourt = ClientRepresentedInCourt;
    }

    /**
     * @return the NegotiationWithCreditorsByDebtAdviser
     */
    public String getNegotiationWithCreditorsByDebtAdviser() {
        return NegotiationWithCreditorsByDebtAdviser;
    }

    /**
     * @param NegotiationWithCreditorsByDebtAdviser the
     * NegotiationWithCreditorsByDebtAdviser to set
     */
    public void setNegotiationWithCreditorsByDebtAdviser(String NegotiationWithCreditorsByDebtAdviser) {
        this.NegotiationWithCreditorsByDebtAdviser = NegotiationWithCreditorsByDebtAdviser;
    }

    /**
     * @return the IncomeMaximisation
     */
    public String getIncomeMaximisation() {
        return IncomeMaximisation;
    }

    /**
     * @param IncomeMaximisation the IncomeMaximisation to set
     */
    public void setIncomeMaximisation(String IncomeMaximisation) {
        this.IncomeMaximisation = IncomeMaximisation;
    }

    /**
     * @return the Other
     */
    public String getOther() {
        return Other;
    }

    /**
     * @param Other the Other to set
     */
    public void setOther(String Other) {
        this.Other = Other;
    }

    /**
     * @return the NoBankAccount
     */
    public String getNoBankAccount() {
        return NoBankAccount;
    }

    /**
     * @param NoBankAccount the NoBankAccount to set
     */
    public void setNoBankAccount(String NoBankAccount) {
        this.NoBankAccount = NoBankAccount;
    }

    /**
     * @return the NoSavingsHeld
     */
    public String getNoSavingsHeld() {
        return NoSavingsHeld;
    }

    /**
     * @param NoSavingsHeld the NoSavingsHeld to set
     */
    public void setNoSavingsHeld(String NoSavingsHeld) {
        this.NoSavingsHeld = NoSavingsHeld;
    }

    /**
     * @return the UserOfHighInterestCredit
     */
    public String getUserOfHighInterestCredit() {
        return UserOfHighInterestCredit;
    }

    /**
     * @param UserOfHighInterestCredit the UserOfHighInterestCredit to set
     */
    public void setUserOfHighInterestCredit(String UserOfHighInterestCredit) {
        this.UserOfHighInterestCredit = UserOfHighInterestCredit;
    }

    /**
     * @return the PriorityDebtsOwed
     */
    public String getPriorityDebtsOwed() {
        return PriorityDebtsOwed;
    }

    /**
     * @param PriorityDebtsOwed the PriorityDebtsOwed to set
     */
    public void setPriorityDebtsOwed(String PriorityDebtsOwed) {
        this.PriorityDebtsOwed = PriorityDebtsOwed;
    }

    /**
     * @return the IndividualIncomeLT14500
     */
    public String getIndividualIncomeLT14500() {
        return IndividualIncomeLT14500;
    }

    /**
     * @param IndividualIncomeLT14500 the IndividualIncomeLT14500 to set
     */
    public void setIndividualIncomeLT14500(String IndividualIncomeLT14500) {
        this.IndividualIncomeLT14500 = IndividualIncomeLT14500;
    }

    /**
     * @return the HouseholdIncomeLT15600
     */
    public String getHouseholdIncomeLT15600() {
        return HouseholdIncomeLT15600;
    }

    /**
     * @param HouseholdIncomeLT15600 the HouseholdIncomeLT15600 to set
     */
    public void setHouseholdIncomeLT15600(String HouseholdIncomeLT15600) {
        this.HouseholdIncomeLT15600 = HouseholdIncomeLT15600;
    }

    /**
     * @return the NoHomeContentsInsurance
     */
    public String getNoHomeContentsInsurance() {
        return NoHomeContentsInsurance;
    }

    /**
     * @param NoHomeContentsInsurance the NoHomeContentsInsurance to set
     */
    public void setNoHomeContentsInsurance(String NoHomeContentsInsurance) {
        this.NoHomeContentsInsurance = NoHomeContentsInsurance;
    }

    /**
     * @return the PostcodeSector
     */
    public String getPostcodeSector() {
        return PostcodeSector;
    }

    /**
     * @param PostcodeSector the PostcodeSector to set
     */
    public void setPostcodeSector(String PostcodeSector) {
        this.PostcodeSector = PostcodeSector;
    }

    /**
     * @return the NumberOfFinancialExclusionIndicators
     */
    public String getNumberOfFinancialExclusionIndicators() {
        return NumberOfFinancialExclusionIndicators;
    }

    /**
     * @param NumberOfFinancialExclusionIndicators the
     * NumberOfFinancialExclusionIndicators to set
     */
    public void setNumberOfFinancialExclusionIndicators(String NumberOfFinancialExclusionIndicators) {
        this.NumberOfFinancialExclusionIndicators = NumberOfFinancialExclusionIndicators;
    }

    /**
     * @return the InitialChannelUsedToDeliverDebtAdvice
     */
    public String getInitialChannelUsedToDeliverDebtAdvice() {
        return InitialChannelUsedToDeliverDebtAdvice;
    }

    /**
     * @param InitialChannelUsedToDeliverDebtAdvice the
     * InitialChannelUsedToDeliverDebtAdvice to set
     */
    public void setInitialChannelUsedToDeliverDebtAdvice(String InitialChannelUsedToDeliverDebtAdvice) {
        this.InitialChannelUsedToDeliverDebtAdvice = InitialChannelUsedToDeliverDebtAdvice;
    }

    /**
     * @return the TypeOfAdvice
     */
    public String getTypeOfAdvice() {
        return TypeOfAdvice;
    }

    /**
     * @param TypeOfAdvice the TypeOfAdvice to set
     */
    public void setTypeOfAdvice(String TypeOfAdvice) {
        this.TypeOfAdvice = TypeOfAdvice;
    }

    /**
     * @return the AgreeableToEvaluation
     */
    public String getAgreeableToEvaluation() {
        return AgreeableToEvaluation;
    }

    /**
     * @param AgreeableToEvaluation the AgreeableToEvaluation to set
     */
    public void setAgreeableToEvaluation(String AgreeableToEvaluation) {
        this.AgreeableToEvaluation = AgreeableToEvaluation;
    }

}
