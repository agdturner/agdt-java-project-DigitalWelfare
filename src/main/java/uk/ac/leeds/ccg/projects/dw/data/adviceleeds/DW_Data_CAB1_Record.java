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

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;

/**
 *
 * @author geoagdt
 */
public class DW_Data_CAB1_Record extends DW_Data_Postcode_Record {

    /**
     * 0 1 RecordType
     */
    private String RecordType;
    /**
     * 1 2 HousingBenefitClaimReferenceNumber
     */

    //Client Profile ID,,,,,,,Client GUID,,,,,,,,,,,,,Age Group,Age,,Gender,Disability,,,Type Of Disability,Local Code1,Local Code2,Local Code3,Local Code4,Local Code5,Local Code6,Occupation,Ethnicity,Nationality,First Language,Second Language,Preferred Method Of Contact,Income Profile,Household Type,Housing Tenure,Marital Status,Child Dependants Under14,Child Dependants Over14, Postal Code,Local Authority,Local Authority Ward,Parliamentary Constituency, Westminster MP, Primary Care Trust,Date Of Birth,Religion,Sexual Orientation
    private String ClientProfileID;
    private String ClientGUID;
    private String AgeGroup;
    private String Age;
    private String Gender;
    private String Disability;
    private String TypeOfDisability;
    private String LocalCode1;
    private String LocalCode2;
    private String LocalCode3;
    private String LocalCode4;
    private String LocalCode5;
    private String LocalCode6;
    private String Occupation;
    private String Ethnicity;
    private String Nationality;
    private String FirstLanguage;
    private String SecondLanguage;
    private String PreferredMethodOfContact;
    private String IncomeProfile;
    private String HouseholdType;
    private String HousingTenure;
    private String MaritalStatus;
    private String ChildDependantsUnder14;
    private String ChildDependantsOver14;
//    private String PostalCode;
    private String LocalAuthority;
    private String LocalAuthorityWard;
    private String ParliamentaryConstituency;
    private String WestminsterMP;
    private String PrimaryCareTrust;
    private String DateOfBirth;
    private String Religion;
    private String SexualOrientation;

    /**
     * Creates a null record in case this is needed
     *
     * @param RecordID
     */
    public DW_Data_CAB1_Record(
            DW_Environment env) {
        super(env);
    }

    /**
     * @param env
     * @param RecordID
     * @param line
     * @param handler
     * @throws java.lang.Exception
     */
    public DW_Data_CAB1_Record(
            DW_Environment env,
            long RecordID,
            String line,
            DW_Data_CAB1_Handler handler) throws Exception {
        super(env);
        setRecordID(RecordID);
        String[] fields = line.split(",");
        if (fields.length != 36) {
                    System.out.println("fields.length " + fields.length);
                    System.out.println("RecordID " + RecordID);
                    System.out.println(line);
        }
        int n = 0;
        ClientProfileID = fields[n];
        n += 7;
        ClientGUID = fields[n];
        n += 13;
        AgeGroup = fields[n];
        n++;
        Age = fields[n];
        n += 2;
        Gender = fields[n];
        n++;
        Disability = fields[n];
        n += 3;
        TypeOfDisability = fields[n];
        n++;
        LocalCode1 = fields[n];
        n++;
        LocalCode2 = fields[n];
        n++;
        LocalCode3 = fields[n];
        n++;
        LocalCode4 = fields[n];
        n++;
        LocalCode5 = fields[n];
        n++;
        LocalCode6 = fields[n];
        n++;
        Occupation = fields[n];
        n++;
        Ethnicity = fields[n];
        n++;
        Nationality = fields[n];
        n++;
        FirstLanguage = fields[n];
        n++;
        SecondLanguage = fields[n];
        n++;
        PreferredMethodOfContact = fields[n];
        n++;
        IncomeProfile = fields[n];
        n++;
        HouseholdType = fields[n];
        n++;
        HousingTenure = fields[n];
        n++;
        MaritalStatus = fields[n];
        n++;
        ChildDependantsUnder14 = fields[n];
        n++;
        ChildDependantsOver14 = fields[n];
        n++;
        setPostcode(fields[n]);
        n++;
        LocalAuthority = fields[n];
        n++;
        LocalAuthorityWard = fields[n];
        n++;
        ParliamentaryConstituency = fields[n];
        n++;
        WestminsterMP = fields[n];
        n++;
        PrimaryCareTrust = fields[n];
        n++;
        DateOfBirth = fields[n];
        n++;
        Religion = fields[n];
        n++;
        SexualOrientation = fields[n];
    }

    @Override
    public String toString() {
        return super.toString()
                + ",ClientGUID " + ClientGUID
                + ",AgeGroup " + AgeGroup
                + ",Age " + Age
                + ",Gender " + Gender
                + ",Disability " + Disability
                + ",TypeOfDisability " + TypeOfDisability
                + ",LocalCode1 " + LocalCode1
                + ",LocalCode2 " + LocalCode2
                + ",LocalCode3 " + LocalCode3
                + ",LocalCode4 " + LocalCode4
                + ",LocalCode5 " + LocalCode5
                + ",LocalCode6 " + LocalCode6
                + ",Occupation " + Occupation
                + ",Ethnicity " + Ethnicity
                + ",Nationality " + Nationality
                + ",FirstLanguage " + FirstLanguage
                + ",SecondLanguage " + SecondLanguage
                + ",PreferredMethodOfContact " + PreferredMethodOfContact
                + ",IncomeProfile " + IncomeProfile
                + ",HouseholdType " + HouseholdType
                + ",HousingTenure " + HousingTenure
                + ",MaritalStatus " + MaritalStatus
                + ",ChildDependantsUnder14 " + ChildDependantsUnder14
                + ",ChildDependantsOver14 " + ChildDependantsOver14
//                + ",PostalCode " + PostalCode
                + ",LocalAuthority " + LocalAuthority
                + ",LocalAuthorityWard " + LocalAuthorityWard
                + ",ParliamentaryConstituency " + ParliamentaryConstituency
                + ",WestminsterMP " + WestminsterMP
                + ",PrimaryCareTrust " + PrimaryCareTrust
                + ",DateOfBirth " + DateOfBirth
                + ",Religion " + Religion
                + ",SexualOrientation " + SexualOrientation;
    }

    public String getRecordType() {
        return RecordType;
    }

    public void setRecordType(String RecordType) {
        this.RecordType = RecordType;
    }

    public String getClientProfileID() {
        return ClientProfileID;
    }

    public void setClientProfileID(String ClientProfileID) {
        this.ClientProfileID = ClientProfileID;
    }

    public String getClientGUID() {
        return ClientGUID;
    }

    public void setClientGUID(String ClientGUID) {
        this.ClientGUID = ClientGUID;
    }

    public String getAgeGroup() {
        return AgeGroup;
    }

    public void setAgeGroup(String AgeGroup) {
        this.AgeGroup = AgeGroup;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String Age) {
        this.Age = Age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getDisability() {
        return Disability;
    }

    public void setDisability(String Disability) {
        this.Disability = Disability;
    }

    public String getTypeOfDisability() {
        return TypeOfDisability;
    }

    public void setTypeOfDisability(String TypeOfDisability) {
        this.TypeOfDisability = TypeOfDisability;
    }

    public String getLocalCode1() {
        return LocalCode1;
    }

    public void setLocalCode1(String LocalCode1) {
        this.LocalCode1 = LocalCode1;
    }

    public String getLocalCode2() {
        return LocalCode2;
    }

    public void setLocalCode2(String LocalCode2) {
        this.LocalCode2 = LocalCode2;
    }

    public String getLocalCode3() {
        return LocalCode3;
    }

    public void setLocalCode3(String LocalCode3) {
        this.LocalCode3 = LocalCode3;
    }

    public String getLocalCode4() {
        return LocalCode4;
    }

    public void setLocalCode4(String LocalCode4) {
        this.LocalCode4 = LocalCode4;
    }

    public String getLocalCode5() {
        return LocalCode5;
    }

    public void setLocalCode5(String LocalCode5) {
        this.LocalCode5 = LocalCode5;
    }

    public String getLocalCode6() {
        return LocalCode6;
    }

    public void setLocalCode6(String LocalCode6) {
        this.LocalCode6 = LocalCode6;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String Occupation) {
        this.Occupation = Occupation;
    }

    public String getEthnicity() {
        return Ethnicity;
    }

    public void setEthnicity(String Ethnicity) {
        this.Ethnicity = Ethnicity;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String Nationality) {
        this.Nationality = Nationality;
    }

    public String getFirstLanguage() {
        return FirstLanguage;
    }

    public void setFirstLanguage(String FirstLanguage) {
        this.FirstLanguage = FirstLanguage;
    }

    public String getSecondLanguage() {
        return SecondLanguage;
    }

    public void setSecondLanguage(String SecondLanguage) {
        this.SecondLanguage = SecondLanguage;
    }

    public String getPreferredMethodOfContact() {
        return PreferredMethodOfContact;
    }

    public void setPreferredMethodOfContact(String PreferredMethodOfContact) {
        this.PreferredMethodOfContact = PreferredMethodOfContact;
    }

    public String getIncomeProfile() {
        return IncomeProfile;
    }

    public void setIncomeProfile(String IncomeProfile) {
        this.IncomeProfile = IncomeProfile;
    }

    public String getHouseholdType() {
        return HouseholdType;
    }

    public void setHouseholdType(String HouseholdType) {
        this.HouseholdType = HouseholdType;
    }

    public String getHousingTenure() {
        return HousingTenure;
    }

    public void setHousingTenure(String HousingTenure) {
        this.HousingTenure = HousingTenure;
    }

    public String getMaritalStatus() {
        return MaritalStatus;
    }

    public void setMaritalStatus(String MaritalStatus) {
        this.MaritalStatus = MaritalStatus;
    }

    public String getChildDependantsUnder14() {
        return ChildDependantsUnder14;
    }

    public void setChildDependantsUnder14(String ChildDependantsUnder14) {
        this.ChildDependantsUnder14 = ChildDependantsUnder14;
    }

    public String getChildDependantsOver14() {
        return ChildDependantsOver14;
    }

    public void setChildDependantsOver14(String ChildDependantsOver14) {
        this.ChildDependantsOver14 = ChildDependantsOver14;
    }

    public String getLocalAuthority() {
        return LocalAuthority;
    }

    public void setLocalAuthority(String LocalAuthority) {
        this.LocalAuthority = LocalAuthority;
    }

    public String getLocalAuthorityWard() {
        return LocalAuthorityWard;
    }

    public void setLocalAuthorityWard(String LocalAuthorityWard) {
        this.LocalAuthorityWard = LocalAuthorityWard;
    }

    public String getParliamentaryConstituency() {
        return ParliamentaryConstituency;
    }

    public void setParliamentaryConstituency(String ParliamentaryConstituency) {
        this.ParliamentaryConstituency = ParliamentaryConstituency;
    }

    public String getWestminsterMP() {
        return WestminsterMP;
    }

    public void setWestminsterMP(String WestminsterMP) {
        this.WestminsterMP = WestminsterMP;
    }

    public String getPrimaryCareTrust() {
        return PrimaryCareTrust;
    }

    public void setPrimaryCareTrust(String PrimaryCareTrust) {
        this.PrimaryCareTrust = PrimaryCareTrust;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String DateOfBirth) {
        this.DateOfBirth = DateOfBirth;
    }

    public String getReligion() {
        return Religion;
    }

    public void setReligion(String Religion) {
        this.Religion = Religion;
    }

    public String getSexualOrientation() {
        return SexualOrientation;
    }

    public void setSexualOrientation(String SexualOrientation) {
        this.SexualOrientation = SexualOrientation;
    }
    
    
}
